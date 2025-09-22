/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.websocket

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WebSocketClientTest {

    private lateinit var webSocketClient: WebSocketClient
    private val mockOkHttpClient = mockk<OkHttpClient>()
    private val mockWebSocket = mockk<WebSocket>()
    private val mockResponse = mockk<Response>()

    @Before
    fun setup() {
        webSocketClient = WebSocketClient(mockOkHttpClient)
    }

    @Test
    fun `connect establishes websocket connection`() = runTest {
        // Given
        every { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) } returns mockWebSocket

        // When
        webSocketClient.connect()

        // Then
        verify { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) }
        assertTrue(webSocketClient.isConnected.value)
    }

    @Test
    fun `disconnect closes websocket connection`() = runTest {
        // Given
        every { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) } returns mockWebSocket
        every { mockWebSocket.close(any(), any()) } returns true
        webSocketClient.connect()

        // When
        webSocketClient.disconnect()

        // Then
        verify { mockWebSocket.close(1000, "Client disconnecting") }
        assertFalse(webSocketClient.isConnected.value)
    }

    @Test
    fun `subscribeToLiveUpdates sends subscription message`() = runTest {
        // Given
        every { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) } returns mockWebSocket
        every { mockWebSocket.send(any<String>()) } returns true
        webSocketClient.connect()

        // When
        webSocketClient.subscribeToLiveUpdates()

        // Then
        verify { mockWebSocket.send(any<String>()) }
    }

    @Test
    fun `matchUpdates flow emits when message received`() = runTest {
        // Given
        every { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) } returns mockWebSocket

        // Capture the WebSocketListener
        var capturedListener: WebSocketListener? = null
        every { mockOkHttpClient.newWebSocket(any<Request>(), capture(slot<WebSocketListener>())) } answers {
            capturedListener = slot<WebSocketListener>().captured
            mockWebSocket
        }

        webSocketClient.connect()

        // When
        val testMessage = """
            {
                "type": "match_update",
                "data": {
                    "matchId": "1",
                    "homeScore": 2,
                    "awayScore": 1,
                    "minute": 67,
                    "status": "LIVE"
                }
            }
        """.trimIndent()

        // Simulate message received
        capturedListener?.onMessage(mockWebSocket, testMessage)

        // Then
        val updates = webSocketClient.matchUpdates.toList()
        assertTrue(updates.isNotEmpty())
    }

    @Test
    fun `onFailure sets connection status to false`() = runTest {
        // Given
        every { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) } returns mockWebSocket

        var capturedListener: WebSocketListener? = null
        every { mockOkHttpClient.newWebSocket(any<Request>(), capture(slot<WebSocketListener>())) } answers {
            capturedListener = slot<WebSocketListener>().captured
            mockWebSocket
        }

        webSocketClient.connect()
        assertTrue(webSocketClient.isConnected.value)

        // When
        val testException = Exception("Connection failed")
        capturedListener?.onFailure(mockWebSocket, testException, null)

        // Then
        assertFalse(webSocketClient.isConnected.value)
    }

    @Test
    fun `onClosed sets connection status to false`() = runTest {
        // Given
        every { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) } returns mockWebSocket

        var capturedListener: WebSocketListener? = null
        every { mockOkHttpClient.newWebSocket(any<Request>(), capture(slot<WebSocketListener>())) } answers {
            capturedListener = slot<WebSocketListener>().captured
            mockWebSocket
        }

        webSocketClient.connect()
        assertTrue(webSocketClient.isConnected.value)

        // When
        capturedListener?.onClosed(mockWebSocket, 1000, "Normal closure")

        // Then
        assertFalse(webSocketClient.isConnected.value)
    }

    @Test
    fun `reconnect disconnects and connects again`() = runTest {
        // Given
        every { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) } returns mockWebSocket
        every { mockWebSocket.close(any(), any()) } returns true
        webSocketClient.connect()

        // When
        webSocketClient.reconnect()

        // Then
        verify { mockWebSocket.close(1000, "Client disconnecting") }
        verify(exactly = 2) { mockOkHttpClient.newWebSocket(any<Request>(), any<WebSocketListener>()) }
    }

    @Test
    fun `initial connection status is false`() {
        // Then
        assertFalse(webSocketClient.isConnected.value)
    }
}