/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.websocket

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    private val TAG = "WebSocketClient"

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var webSocket: WebSocket? = null
    private var isConnecting = false
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 5
    private val baseReconnectDelay = 1000L // 1 second

    // WebSocket events flow
    private val _events = MutableSharedFlow<WebSocketEvent>(
        replay = 0,
        extraBufferCapacity = 100,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<WebSocketEvent> = _events.asSharedFlow()

    // Connection state flow
    private val _connectionState = MutableSharedFlow<ConnectionState>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionState: SharedFlow<ConnectionState> = _connectionState.asSharedFlow()

    // Live match updates flow
    private val _liveUpdates = MutableSharedFlow<LiveMatchUpdate>(
        replay = 0,
        extraBufferCapacity = 50,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val liveUpdates: SharedFlow<LiveMatchUpdate> = _liveUpdates.asSharedFlow()

    init {
        _connectionState.tryEmit(ConnectionState.Disconnected)
    }

    fun connect(url: String = DEFAULT_WEBSOCKET_URL) {
        if (isConnecting || webSocket != null) {
            Log.d(TAG, "Already connected or connecting")
            return
        }

        isConnecting = true
        _connectionState.tryEmit(ConnectionState.Connecting)

        val request = Request.Builder()
            .url(url)
            .addHeader("Origin", "https://score24seven.com")
            .build()

        webSocket = okHttpClient.newWebSocket(request, createWebSocketListener())
    }

    fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket")
        isConnecting = false
        reconnectAttempts = 0
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        _connectionState.tryEmit(ConnectionState.Disconnected)
    }

    fun subscribeToMatch(matchId: Int) {
        sendMessage(
            WebSocketMessage(
                type = "subscribe",
                data = mapOf("matchId" to matchId)
            )
        )
    }

    fun unsubscribeFromMatch(matchId: Int) {
        sendMessage(
            WebSocketMessage(
                type = "unsubscribe",
                data = mapOf("matchId" to matchId)
            )
        )
    }

    fun subscribeToLiveMatches() {
        sendMessage(
            WebSocketMessage(
                type = "subscribe_live",
                data = emptyMap()
            )
        )
    }

    private fun sendMessage(message: WebSocketMessage) {
        val json = gson.toJson(message)
        val success = webSocket?.send(json) ?: false

        if (!success) {
            Log.w(TAG, "Failed to send WebSocket message: $json")
        } else {
            Log.d(TAG, "Sent WebSocket message: $json")
        }
    }

    private fun createWebSocketListener() = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "WebSocket connected")
            isConnecting = false
            reconnectAttempts = 0
            _connectionState.tryEmit(ConnectionState.Connected)
            _events.tryEmit(WebSocketEvent.Connected)

            // Subscribe to live matches on connection
            subscribeToLiveMatches()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "WebSocket message received: $text")
            handleMessage(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(TAG, "WebSocket binary message received")
            handleMessage(bytes.utf8())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "WebSocket closing: $code $reason")
            _connectionState.tryEmit(ConnectionState.Disconnecting)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "WebSocket closed: $code $reason")
            this@WebSocketClient.webSocket = null
            _connectionState.tryEmit(ConnectionState.Disconnected)
            _events.tryEmit(WebSocketEvent.Disconnected(code, reason))

            // Attempt reconnection if not user-initiated
            if (code != 1000 && reconnectAttempts < maxReconnectAttempts) {
                attemptReconnection()
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e(TAG, "WebSocket failure", t)
            this@WebSocketClient.webSocket = null
            isConnecting = false
            _connectionState.tryEmit(ConnectionState.Error(t))
            _events.tryEmit(WebSocketEvent.Error(t))

            // Attempt reconnection
            if (reconnectAttempts < maxReconnectAttempts) {
                attemptReconnection()
            }
        }
    }

    private fun handleMessage(message: String) {
        try {
            val webSocketMessage = gson.fromJson(message, WebSocketMessage::class.java)

            when (webSocketMessage.type) {
                "live_update" -> {
                    val update = gson.fromJson(
                        gson.toJson(webSocketMessage.data),
                        LiveMatchUpdate::class.java
                    )
                    _liveUpdates.tryEmit(update)
                }

                "match_event" -> {
                    val event = gson.fromJson(
                        gson.toJson(webSocketMessage.data),
                        com.score24seven.data.websocket.MatchEvent::class.java
                    )
                    _events.tryEmit(WebSocketEvent.MatchEvent(event))
                }

                "score_update" -> {
                    val scoreUpdate = gson.fromJson(
                        gson.toJson(webSocketMessage.data),
                        com.score24seven.data.websocket.ScoreUpdate::class.java
                    )
                    _events.tryEmit(WebSocketEvent.ScoreUpdate(scoreUpdate))
                }

                "status_update" -> {
                    val statusUpdate = gson.fromJson(
                        gson.toJson(webSocketMessage.data),
                        com.score24seven.data.websocket.StatusUpdate::class.java
                    )
                    _events.tryEmit(WebSocketEvent.StatusUpdate(statusUpdate))
                }

                "ping" -> {
                    // Respond to ping with pong
                    sendMessage(WebSocketMessage(type = "pong", data = emptyMap()))
                }

                "error" -> {
                    val error = webSocketMessage.data["message"] as? String ?: "Unknown error"
                    _events.tryEmit(WebSocketEvent.Error(Exception(error)))
                }

                else -> {
                    Log.w(TAG, "Unknown WebSocket message type: ${webSocketMessage.type}")
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error parsing WebSocket message", e)
            _events.tryEmit(WebSocketEvent.Error(e))
        }
    }

    private fun attemptReconnection() {
        reconnectAttempts++
        val delay = baseReconnectDelay * (1 shl (reconnectAttempts - 1)) // Exponential backoff

        Log.d(TAG, "Attempting reconnection #$reconnectAttempts in ${delay}ms")

        scope.launch {
            delay(delay)
            if (reconnectAttempts <= maxReconnectAttempts) {
                connect()
            }
        }
    }

    companion object {
        private const val DEFAULT_WEBSOCKET_URL = "wss://ws.score24seven.com/live"
    }
}

// Data classes for WebSocket communication
data class WebSocketMessage(
    val type: String,
    val data: Map<String, Any>
)

sealed class WebSocketEvent {
    object Connected : WebSocketEvent()
    data class Disconnected(val code: Int, val reason: String) : WebSocketEvent()
    data class Error(val throwable: Throwable) : WebSocketEvent()
    data class MatchEvent(val event: com.score24seven.data.websocket.MatchEvent) : WebSocketEvent()
    data class ScoreUpdate(val update: com.score24seven.data.websocket.ScoreUpdate) : WebSocketEvent()
    data class StatusUpdate(val update: com.score24seven.data.websocket.StatusUpdate) : WebSocketEvent()
}

sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    object Disconnecting : ConnectionState()
    data class Error(val throwable: Throwable) : ConnectionState()
}

data class LiveMatchUpdate(
    val matchId: Int,
    val homeScore: Int?,
    val awayScore: Int?,
    val status: String,
    val elapsed: Int?,
    val timestamp: Long
)

data class MatchEvent(
    val matchId: Int,
    val timeElapsed: Int,
    val timeExtra: Int?,
    val teamId: Int,
    val playerId: Int,
    val playerName: String,
    val type: String, // "Goal", "Card", "Substitution"
    val detail: String,
    val timestamp: Long
)

data class ScoreUpdate(
    val matchId: Int,
    val homeScore: Int?,
    val awayScore: Int?,
    val timestamp: Long
)

data class StatusUpdate(
    val matchId: Int,
    val status: String,
    val statusLong: String,
    val elapsed: Int?,
    val timestamp: Long
)