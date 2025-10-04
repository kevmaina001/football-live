/*
 * Preferences Manager for Score24Seven Settings
 */

package com.score24seven.util

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "score24seven_prefs"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_LIVE_SCORE_NOTIFICATIONS = "live_score_notifications"
        private const val KEY_MATCH_REMINDERS = "match_reminders"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_FAVORITE_MATCHES = "favorite_matches"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Dark mode state
    private val _isDarkMode = MutableStateFlow(getDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        println("🎨 DEBUG: PreferencesManager - Initialized with dark mode: ${getDarkMode()}")
    }

    // Notifications state
    private val _notificationsEnabled = MutableStateFlow(getNotificationsEnabled())
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _liveScoreNotifications = MutableStateFlow(getLiveScoreNotifications())
    val liveScoreNotifications: StateFlow<Boolean> = _liveScoreNotifications.asStateFlow()

    private val _matchReminders = MutableStateFlow(getMatchReminders())
    val matchReminders: StateFlow<Boolean> = _matchReminders.asStateFlow()

    // Language state
    private val _language = MutableStateFlow(getLanguage())
    val language: StateFlow<String> = _language.asStateFlow()

    // Favorite matches state - reactive StateFlow
    private val _favoriteMatchIds = MutableStateFlow(getFavoriteMatchIds())
    val favoriteMatchIds: StateFlow<List<Int>> = _favoriteMatchIds.asStateFlow()

    // Dark Mode
    fun getDarkMode(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE, false) // Default to light mode
    }

    fun setDarkMode(isDark: Boolean) {
        println("🎨 DEBUG: PreferencesManager - Setting dark mode to: $isDark")
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply()
        _isDarkMode.value = isDark
        println("🎨 DEBUG: PreferencesManager - Dark mode state updated to: ${_isDarkMode.value}")
    }

    // Notifications
    fun getNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
        _notificationsEnabled.value = enabled
    }

    fun getLiveScoreNotifications(): Boolean {
        return prefs.getBoolean(KEY_LIVE_SCORE_NOTIFICATIONS, true)
    }

    fun setLiveScoreNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LIVE_SCORE_NOTIFICATIONS, enabled).apply()
        _liveScoreNotifications.value = enabled
    }

    fun getMatchReminders(): Boolean {
        return prefs.getBoolean(KEY_MATCH_REMINDERS, true)
    }

    fun setMatchReminders(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_MATCH_REMINDERS, enabled).apply()
        _matchReminders.value = enabled
    }

    // Language
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun setLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
        _language.value = language
    }

    // First Launch
    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchCompleted() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }

    // Favorite Matches - now with reactive updates
    fun getFavoriteMatchIds(): List<Int> {
        val favoritesString = prefs.getString(KEY_FAVORITE_MATCHES, "") ?: ""
        return if (favoritesString.isBlank()) {
            emptyList()
        } else {
            try {
                favoritesString.split(",").map { it.trim().toInt() }
            } catch (e: Exception) {
                println("❌ ERROR: Failed to parse favorite matches: ${e.message}")
                emptyList()
            }
        }
    }

    fun setFavoriteMatchIds(matchIds: List<Int>) {
        val favoritesString = matchIds.joinToString(",")
        prefs.edit().putString(KEY_FAVORITE_MATCHES, favoritesString).apply()
        _favoriteMatchIds.value = matchIds // Update StateFlow to trigger observers
        println("💖 DEBUG: Saved favorite matches: $favoritesString - ${matchIds.size} favorites")
    }

    // Clear all preferences
    fun clearAll() {
        prefs.edit().clear().apply()
        // Reset state flows to defaults (light mode by default)
        _isDarkMode.value = false // Light mode default
        _notificationsEnabled.value = true
        _liveScoreNotifications.value = true
        _matchReminders.value = true
        _language.value = "en"
        _favoriteMatchIds.value = emptyList()
    }
}
