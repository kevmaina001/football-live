/*
 * Settings ViewModel for managing app preferences and settings
 */

package com.score24seven.ui.viewmodel

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.MainActivity
import com.score24seven.Score24SevenApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = Score24SevenApplication.preferencesManager

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        observePreferences()
    }

    private fun loadSettings() {
        _uiState.value = _uiState.value.copy(
            isDarkMode = preferencesManager.getDarkMode(),
            notificationsEnabled = preferencesManager.getNotificationsEnabled(),
            liveScoreNotifications = preferencesManager.getLiveScoreNotifications(),
            matchReminders = preferencesManager.getMatchReminders(),
            selectedLanguage = preferencesManager.getLanguage()
        )
    }

    private fun observePreferences() {
        viewModelScope.launch {
            preferencesManager.isDarkMode.collect { isDark ->
                _uiState.value = _uiState.value.copy(isDarkMode = isDark)
            }
        }

        viewModelScope.launch {
            preferencesManager.notificationsEnabled.collect { enabled ->
                _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
            }
        }

        viewModelScope.launch {
            preferencesManager.liveScoreNotifications.collect { enabled ->
                _uiState.value = _uiState.value.copy(liveScoreNotifications = enabled)
            }
        }

        viewModelScope.launch {
            preferencesManager.matchReminders.collect { enabled ->
                _uiState.value = _uiState.value.copy(matchReminders = enabled)
            }
        }

        viewModelScope.launch {
            preferencesManager.language.collect { language ->
                _uiState.value = _uiState.value.copy(selectedLanguage = language)
            }
        }
    }

    fun toggleDarkMode() {
        val currentValue = _uiState.value.isDarkMode
        val newValue = !currentValue
        println("🎨 DEBUG: SettingsViewModel - Toggling theme from $currentValue to $newValue")
        preferencesManager.setDarkMode(newValue)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                if (enabled) {
                    // Create notification channels when enabling notifications
                    createNotificationChannels()

                    // Check if notifications are actually allowed
                    val notificationManager = NotificationManagerCompat.from(getApplication())
                    if (!notificationManager.areNotificationsEnabled()) {
                        _uiState.value = _uiState.value.copy(
                            message = "Please enable notifications in your device settings for Score24Seven."
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            message = "Notifications enabled successfully."
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        message = "Notifications disabled."
                    )
                }

                preferencesManager.setNotificationsEnabled(enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Failed to update notification settings: ${e.message}"
                )
            }
        }
    }

    fun setLiveScoreNotifications(enabled: Boolean) {
        preferencesManager.setLiveScoreNotifications(enabled)
        _uiState.value = _uiState.value.copy(
            message = if (enabled) "Live score notifications enabled" else "Live score notifications disabled"
        )
    }

    fun setMatchReminders(enabled: Boolean) {
        preferencesManager.setMatchReminders(enabled)
        _uiState.value = _uiState.value.copy(
            message = if (enabled) "Match reminders enabled" else "Match reminders disabled"
        )
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val context = getApplication<Application>()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

            println("📱 DEBUG: Notification channels created")
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Save language preference
                preferencesManager.setLanguage(language)

                // Apply language change to the app
                applyLanguageChange(language)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Language changed to ${SettingsLanguage.entries.find { it.code == language }?.displayName ?: language}. Restart the app to see full effect."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Failed to change language: ${e.message}"
                )
            }
        }
    }

    private fun applyLanguageChange(languageCode: String) {
        try {
            // Set app locale using AppCompatDelegate for immediate effect
            val localeList = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(localeList)

            // Also update the default locale
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            println("🌐 DEBUG: Language changed to $languageCode")
        } catch (e: Exception) {
            println("🔴 ERROR: Failed to apply language change: ${e.message}")
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showClearDataDialog = false,
                isLoading = true
            )

            try {
                // Clear preferences
                preferencesManager.clearAll()

                // Reset to default state
                _uiState.value = SettingsUiState()

                // Show success message
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "All data cleared successfully"
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Failed to clear data: ${e.message}"
                )
            }
        }
    }

    fun showClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = true)
    }

    fun hideClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = false)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun getAppVersion(): String {
        return try {
            val context = getApplication<Application>()
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "${packageInfo.versionName} (${packageInfo.longVersionCode})"
        } catch (e: Exception) {
            "Unknown"
        }
    }
}

data class SettingsUiState(
    val isDarkMode: Boolean = false, // Default to light mode
    val notificationsEnabled: Boolean = true,
    val liveScoreNotifications: Boolean = true,
    val matchReminders: Boolean = true,
    val selectedLanguage: String = "en",
    val showClearDataDialog: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null
)

enum class SettingsLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    SPANISH("es", "Español"),
    FRENCH("fr", "Français"),
    GERMAN("de", "Deutsch"),
    PORTUGUESE("pt", "Português"),
    ITALIAN("it", "Italiano")
}