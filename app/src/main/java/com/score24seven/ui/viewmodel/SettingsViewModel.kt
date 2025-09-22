/*
 * Settings ViewModel for managing app preferences and settings
 */

package com.score24seven.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.Score24SevenApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
        preferencesManager.setNotificationsEnabled(enabled)
    }

    fun setLiveScoreNotifications(enabled: Boolean) {
        preferencesManager.setLiveScoreNotifications(enabled)
    }

    fun setMatchReminders(enabled: Boolean) {
        preferencesManager.setMatchReminders(enabled)
    }

    fun setLanguage(language: String) {
        preferencesManager.setLanguage(language)
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