/*
 * Live Match Monitor Service for instant notifications
 * Monitors favorite matches via WebSocket for real-time updates
 */

package com.score24seven.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.score24seven.data.websocket.LiveMatchService
import com.score24seven.data.websocket.WebSocketEvent
import com.score24seven.domain.model.isLive
import com.score24seven.domain.repository.FavoritesRepository
import com.score24seven.domain.repository.MatchRepository
import com.score24seven.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class LiveMatchMonitorService : Service() {

    @Inject lateinit var liveMatchService: LiveMatchService
    @Inject lateinit var favoritesRepository: FavoritesRepository
    @Inject lateinit var matchRepository: MatchRepository
    @Inject lateinit var notificationService: NotificationService
    @Inject lateinit var preferencesManager: PreferencesManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var pollingJob: Job? = null

    companion object {
        private const val POLLING_INTERVAL_MS = 30_000L // 30 seconds fallback polling
    }

    override fun onCreate() {
        super.onCreate()
        println("🔔 DEBUG: LiveMatchMonitorService - onCreate called")
        startMonitoring()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("🔔 DEBUG: LiveMatchMonitorService - onStartCommand called")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startMonitoring() {
        println("🔔 DEBUG: LiveMatchMonitorService - Starting monitoring")

        // Start WebSocket connection for instant updates
        liveMatchService.startLiveUpdates()

        // Monitor favorite matches and subscribe to WebSocket
        observeFavoriteMatches()

        // Listen to WebSocket events for instant notifications
        observeWebSocketEvents()

        // Start fallback polling
        startPolling()
    }

    private fun observeFavoriteMatches() {
        println("🔔 DEBUG: LiveMatchMonitorService - observeFavoriteMatches started")
        serviceScope.launch {
            favoritesRepository.getFavoriteMatches()
                .collect { favoriteMatches ->
                    println("🔔 DEBUG: LiveMatchMonitorService - Got ${favoriteMatches.size} favorite matches")

                    // Subscribe to WebSocket for live matches only
                    val liveMatchIds = favoriteMatches.filter { it.isLive() }.map { it.id }

                    println("🔔 DEBUG: LiveMatchMonitorService - Subscribing to ${liveMatchIds.size} live matches")
                    liveMatchIds.forEach { matchId ->
                        liveMatchService.subscribeToMatch(matchId)
                    }

                    // Unsubscribe from matches no longer in favorites
                    val currentSubscriptions = liveMatchService.getSubscribedMatches()
                    val toUnsubscribe = currentSubscriptions - liveMatchIds.toSet()
                    toUnsubscribe.forEach { matchId ->
                        liveMatchService.unsubscribeFromMatch(matchId)
                    }
                }
        }
    }

    private fun observeWebSocketEvents() {
        println("🔔 DEBUG: LiveMatchMonitorService - observeWebSocketEvents started")

        // Listen to score updates via WebSocket
        serviceScope.launch {
            liveMatchService.events.collect { event ->
                when (event) {
                    is WebSocketEvent.ScoreUpdate -> {
                        println("⚽ DEBUG: LiveMatchMonitorService - Score update: Match ${event.update.matchId} - ${event.update.homeScore}:${event.update.awayScore}")
                        handleScoreUpdate(event.update.matchId)
                    }
                    is WebSocketEvent.MatchEvent -> {
                        println("🔔 DEBUG: LiveMatchMonitorService - Match event: ${event.event.type} for match ${event.event.matchId}")
                        handleMatchEvent(event.event.matchId)
                    }
                    is WebSocketEvent.StatusUpdate -> {
                        println("🔔 DEBUG: LiveMatchMonitorService - Status update: Match ${event.update.matchId} - ${event.update.status}")
                    }
                    else -> {
                        // Ignore other events
                    }
                }
            }
        }
    }

    private fun startPolling() {
        println("🔔 DEBUG: LiveMatchMonitorService - Starting fallback polling every 30 seconds")
        pollingJob = serviceScope.launch {
            while (isActive) {
                try {
                    // Poll for updates every 30 seconds as fallback
                    delay(POLLING_INTERVAL_MS)

                    if (!preferencesManager.getNotificationsEnabled()) {
                        println("🔔 DEBUG: LiveMatchMonitorService - Notifications disabled, skipping poll")
                        continue
                    }

                    // Refresh favorite matches data
                    favoritesRepository.getFavoriteMatches().firstOrNull()?.let { favoriteMatches ->
                        val liveMatches = favoriteMatches.filter { it.isLive() }
                        println("🔔 DEBUG: LiveMatchMonitorService - Polling ${liveMatches.size} live matches")

                        liveMatches.forEach { match ->
                            notificationService.checkForMatchUpdates(listOf(match))
                        }
                    }
                } catch (e: Exception) {
                    println("🔴 DEBUG: LiveMatchMonitorService - Polling error: ${e.message}")
                }
            }
        }
    }

    private fun handleScoreUpdate(matchId: Int) {
        serviceScope.launch {
            try {
                // Fetch updated match data and trigger notification
                matchRepository.getMatchById(matchId).firstOrNull()?.let { resource ->
                    resource.data?.let { match ->
                        println("⚽ DEBUG: LiveMatchMonitorService - Sending instant notification for match $matchId")
                        notificationService.checkForMatchUpdates(listOf(match))
                    }
                }
            } catch (e: Exception) {
                println("🔴 DEBUG: LiveMatchMonitorService - Error handling score update: ${e.message}")
            }
        }
    }

    private fun handleMatchEvent(matchId: Int) {
        serviceScope.launch {
            try {
                // Fetch updated match data and trigger notification
                matchRepository.getMatchById(matchId).firstOrNull()?.let { resource ->
                    resource.data?.let { match ->
                        println("🔔 DEBUG: LiveMatchMonitorService - Sending notification for match event $matchId")
                        notificationService.checkForMatchUpdates(listOf(match))
                    }
                }
            } catch (e: Exception) {
                println("🔴 DEBUG: LiveMatchMonitorService - Error handling match event: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        println("🔔 DEBUG: LiveMatchMonitorService - onDestroy called")
        pollingJob?.cancel()
        liveMatchService.stopLiveUpdates()
        serviceScope.cancel()
        super.onDestroy()
    }
}
