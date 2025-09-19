/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.websocket

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveMatchService @Inject constructor(
    private val webSocketClient: WebSocketClient
) {
    private val TAG = "LiveMatchService"

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val subscribedMatches = mutableSetOf<Int>()

    val connectionState: SharedFlow<ConnectionState> = webSocketClient.connectionState
    val liveUpdates: SharedFlow<LiveMatchUpdate> = webSocketClient.liveUpdates
    val events: SharedFlow<WebSocketEvent> = webSocketClient.events

    init {
        // Monitor connection state
        connectionState
            .onEach { state ->
                when (state) {
                    is ConnectionState.Connected -> {
                        Log.d(TAG, "Connected to live match service")
                        // Re-subscribe to matches after reconnection
                        resubscribeToMatches()
                    }

                    is ConnectionState.Disconnected -> {
                        Log.d(TAG, "Disconnected from live match service")
                    }

                    is ConnectionState.Error -> {
                        Log.e(TAG, "Live match service error", state.throwable)
                    }

                    else -> {
                        // Handle other states if needed
                    }
                }
            }
            .launchIn(scope)

        // Handle WebSocket events
        events
            .onEach { event ->
                when (event) {
                    is WebSocketEvent.MatchEvent -> {
                        Log.d(TAG, "Match event received: ${event.event}")
                        handleMatchEvent(event.event)
                    }

                    is WebSocketEvent.ScoreUpdate -> {
                        Log.d(TAG, "Score update received: ${event.update}")
                        handleScoreUpdate(event.update)
                    }

                    is WebSocketEvent.StatusUpdate -> {
                        Log.d(TAG, "Status update received: ${event.update}")
                        handleStatusUpdate(event.update)
                    }

                    is WebSocketEvent.Error -> {
                        Log.e(TAG, "WebSocket event error", event.throwable)
                    }

                    else -> {
                        // Handle other events if needed
                    }
                }
            }
            .launchIn(scope)
    }

    fun startLiveUpdates() {
        scope.launch {
            webSocketClient.connect()
        }
    }

    fun stopLiveUpdates() {
        scope.launch {
            subscribedMatches.clear()
            webSocketClient.disconnect()
        }
    }

    fun subscribeToMatch(matchId: Int) {
        scope.launch {
            if (subscribedMatches.add(matchId)) {
                Log.d(TAG, "Subscribing to match: $matchId")
                webSocketClient.subscribeToMatch(matchId)
            }
        }
    }

    fun unsubscribeFromMatch(matchId: Int) {
        scope.launch {
            if (subscribedMatches.remove(matchId)) {
                Log.d(TAG, "Unsubscribing from match: $matchId")
                webSocketClient.unsubscribeFromMatch(matchId)
            }
        }
    }

    fun subscribeToMatches(matchIds: List<Int>) {
        scope.launch {
            matchIds.forEach { matchId ->
                subscribeToMatch(matchId)
            }
        }
    }

    fun unsubscribeFromMatches(matchIds: List<Int>) {
        scope.launch {
            matchIds.forEach { matchId ->
                unsubscribeFromMatch(matchId)
            }
        }
    }

    fun getSubscribedMatches(): Set<Int> = subscribedMatches.toSet()

    private fun resubscribeToMatches() {
        scope.launch {
            val matches = subscribedMatches.toList()
            subscribedMatches.clear()
            matches.forEach { matchId ->
                subscribeToMatch(matchId)
            }
        }
    }

    private fun handleMatchEvent(event: com.kickscore.live.data.websocket.MatchEvent) {
        // This will be handled by repositories/use cases
        // that observe the events flow
        Log.d(TAG, "Processing match event: ${event.type} for match ${event.matchId}")
    }

    private fun handleScoreUpdate(update: com.kickscore.live.data.websocket.ScoreUpdate) {
        Log.d(TAG, "Processing score update for match ${update.matchId}: ${update.homeScore}-${update.awayScore}")
    }

    private fun handleStatusUpdate(update: com.kickscore.live.data.websocket.StatusUpdate) {
        Log.d(TAG, "Processing status update for match ${update.matchId}: ${update.status}")
    }

    // For testing and debugging
    fun isConnected(): Boolean {
        return connectionState.replayCache.lastOrNull() is ConnectionState.Connected
    }

    fun getConnectionInfo(): String {
        val state = connectionState.replayCache.lastOrNull() ?: ConnectionState.Disconnected
        val subscribedCount = subscribedMatches.size
        return "Connection: $state, Subscribed matches: $subscribedCount"
    }
}