/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.score24seven.util.PreferencesManager
import com.score24seven.service.NotificationService
import android.content.Intent
import com.score24seven.service.LiveMatchMonitorService
import com.score24seven.ads.AdManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Score24SevenApplication : Application() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var adManager: AdManager

    companion object {
        lateinit var instance: Score24SevenApplication
            private set

        val preferencesManager: PreferencesManager by lazy {
            PreferencesManager(instance)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize preferences and apply saved settings
        initializeAppSettings()

        // Initialize notification channels
        createNotificationChannels()

        // Initialize any app-level services here
        // - Analytics
        // - Crash reporting
        // - Push notifications
        // - WebSocket service

        // Initialize AdManager for ads
        adManager.toString() // Access the service to ensure it's initialized
        println("📱 DEBUG: AdManager initialized")

        // Initialize notification service for favorite matches
        // This triggers the service to start observing favorite matches
        notificationService.toString() // Access the service to ensure it's initialized
        println("🔔 DEBUG: NotificationService initialized and observing favorite matches")

        // Start LiveMatchMonitorService for instant WebSocket-based notifications
        if (preferencesManager.getNotificationsEnabled()) {
            val serviceIntent = Intent(this, LiveMatchMonitorService::class.java)
            startService(serviceIntent)
            println("🔔 DEBUG: LiveMatchMonitorService started for instant notifications")
        }
    }

    private fun initializeAppSettings() {
        // Apply saved language preference
        val savedLanguage = preferencesManager.getLanguage()
        if (savedLanguage != "en") {
            try {
                val localeList = LocaleListCompat.forLanguageTags(savedLanguage)
                AppCompatDelegate.setApplicationLocales(localeList)
            } catch (e: Exception) {
                println("🔴 ERROR: Failed to apply saved language: ${e.message}")
            }
        }

        // Apply saved theme preference
        val isDarkMode = preferencesManager.getDarkMode()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Live Score Updates Channel
            val liveScoreChannel = NotificationChannel(
                "live_scores",
                "Live Score Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Real-time score updates for live matches"
                enableVibration(true)
                setShowBadge(true)
            }

            // Match Reminders Channel
            val remindersChannel = NotificationChannel(
                "match_reminders",
                "Match Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders for upcoming matches"
                enableVibration(true)
                setShowBadge(true)
            }

            // General Notifications Channel
            val generalChannel = NotificationChannel(
                "general",
                "General Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "General app notifications"
                setShowBadge(false)
            }

            // Create the channels
            notificationManager.createNotificationChannels(listOf(
                liveScoreChannel,
                remindersChannel,
                generalChannel
            ))

            println("📱 DEBUG: Notification channels created on app startup")
        }
    }
}