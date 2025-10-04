/*
 * Notification service for match updates
 */

package com.score24seven.service

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.score24seven.MainActivity
import com.score24seven.R
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.isLive
import com.score24seven.domain.repository.FavoritesRepository
import com.score24seven.util.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favoritesRepository: FavoritesRepository,
    private val preferencesManager: PreferencesManager
) {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val notificationManager = NotificationManagerCompat.from(context)
    private val previousScores = mutableMapOf<Int, Pair<Int?, Int?>>() // matchId to (home, away) score

    companion object {
        private const val CHANNEL_ID_LIVE_SCORES = "live_scores"
        private const val CHANNEL_ID_REMINDERS = "match_reminders"
        private const val NOTIFICATION_ID_BASE = 1000
    }

    init {
        observeFavoriteMatches()
    }

    private fun observeFavoriteMatches() {
        favoritesRepository.getFavoriteMatches()
            .onEach { favoriteMatches ->
                checkForMatchUpdates(favoriteMatches)
            }
            .launchIn(serviceScope)
    }

    private fun checkForMatchUpdates(matches: List<Match>) {
        // Check if notifications are enabled globally
        if (!preferencesManager.getNotificationsEnabled()) return

        matches.forEach { match ->
            when {
                match.isLive() && preferencesManager.getLiveScoreNotifications() -> {
                    // Check for score changes first
                    if (hasMatchScoreChanged(match)) {
                        sendScoreUpdateNotification(match)
                    } else {
                        sendLiveMatchNotification(match)
                    }
                }
                isMatchStartingSoon(match) && preferencesManager.getMatchReminders() -> {
                    sendUpcomingMatchNotification(match)
                }
            }
        }
    }

    private fun sendLiveMatchNotification(match: Match) {
        if (!hasNotificationPermission()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("match_id", match.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            match.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_LIVE_SCORES)
            .setSmallIcon(R.drawable.score24seven_logo)
            .setContentTitle("🔴 LIVE: ${match.homeTeam.name} vs ${match.awayTeam.name}")
            .setContentText("${match.score?.home ?: 0} - ${match.score?.away ?: 0} • ${match.league.name}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        try {
            notificationManager.notify(NOTIFICATION_ID_BASE + match.id, notification)
        } catch (e: SecurityException) {
            println("❌ ERROR: Notification permission not granted")
        }
    }

    private fun sendUpcomingMatchNotification(match: Match) {
        if (!hasNotificationPermission()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("match_id", match.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            match.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.score24seven_logo)
            .setContentTitle("⏰ Match Starting Soon")
            .setContentText("${match.homeTeam.name} vs ${match.awayTeam.name} starts in 15 minutes")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(NOTIFICATION_ID_BASE + match.id + 10000, notification)
        } catch (e: SecurityException) {
            println("❌ ERROR: Notification permission not granted")
        }
    }

    private fun sendScoreUpdateNotification(match: Match) {
        if (!hasNotificationPermission()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("match_id", match.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            match.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_LIVE_SCORES)
            .setSmallIcon(R.drawable.score24seven_logo)
            .setContentTitle("⚽ Goal! ${match.homeTeam.name} vs ${match.awayTeam.name}")
            .setContentText("${match.score?.home ?: 0} - ${match.score?.away ?: 0}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 300, 100, 300, 100, 300))
            .build()

        try {
            notificationManager.notify(NOTIFICATION_ID_BASE + match.id + 20000, notification)
        } catch (e: SecurityException) {
            println("❌ ERROR: Notification permission not granted")
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            notificationManager.areNotificationsEnabled()
        }
    }

    private fun isMatchStartingSoon(match: Match): Boolean {
        // Check if match starts within 15 minutes
        val currentTime = System.currentTimeMillis() / 1000
        val matchTime = match.fixture.timestamp
        val timeDiff = matchTime - currentTime
        return timeDiff in 1..900 // 15 minutes in seconds
    }

    private fun hasMatchScoreChanged(match: Match): Boolean {
        val currentScore = match.score?.let { it.home to it.away } ?: return false
        val previousScore = previousScores[match.id]

        return if (previousScore == null) {
            // First time seeing this match, store its score
            previousScores[match.id] = currentScore
            false
        } else {
            // Compare with previous score
            val hasChanged = previousScore != currentScore
            if (hasChanged) {
                // Update stored score
                previousScores[match.id] = currentScore
                println("⚽ DEBUG: Score changed for match ${match.id}: $previousScore -> $currentScore")
            }
            hasChanged
        }
    }

    fun cancelNotificationForMatch(matchId: Int) {
        notificationManager.cancel(NOTIFICATION_ID_BASE + matchId)
        notificationManager.cancel(NOTIFICATION_ID_BASE + matchId + 10000)
        notificationManager.cancel(NOTIFICATION_ID_BASE + matchId + 20000)
        // Clean up score tracking
        previousScores.remove(matchId)
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
        previousScores.clear()
    }
}
