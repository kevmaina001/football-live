/*
 * Comprehensive Settings Screen with working features
 * Dark/Light mode, Notifications, About, Privacy Policy, etc.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.score24seven.ui.viewmodel.SettingsViewModel
import com.score24seven.ui.viewmodel.SettingsLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Show snackbar for messages
    uiState.message?.let { message ->
        LaunchedEffect(message) {
            // Auto clear message after showing
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            item {
                SettingsSection(title = "Appearance") {
                    SettingsItem(
                        icon = Icons.Default.Settings,
                        title = "Dark Mode",
                        subtitle = if (uiState.isDarkMode) "Dark theme enabled" else "Light theme enabled",
                        trailing = {
                            Switch(
                                checked = uiState.isDarkMode,
                                onCheckedChange = { viewModel.toggleDarkMode() }
                            )
                        }
                    )
                }
            }

            // Notifications Section
            item {
                SettingsSection(title = "Notifications") {
                    SettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "Enable Notifications",
                        subtitle = "Receive app notifications",
                        trailing = {
                            Switch(
                                checked = uiState.notificationsEnabled,
                                onCheckedChange = viewModel::setNotificationsEnabled
                            )
                        }
                    )

                    if (uiState.notificationsEnabled) {
                        SettingsItem(
                            icon = Icons.Default.Notifications,
                            title = "Live Score Updates",
                            subtitle = "Get notified about live match updates",
                            trailing = {
                                Switch(
                                    checked = uiState.liveScoreNotifications,
                                    onCheckedChange = viewModel::setLiveScoreNotifications
                                )
                            }
                        )

                        SettingsItem(
                            icon = Icons.Default.Settings,
                            title = "Match Reminders",
                            subtitle = "Remind me before matches start",
                            trailing = {
                                Switch(
                                    checked = uiState.matchReminders,
                                    onCheckedChange = viewModel::setMatchReminders
                                )
                            }
                        )
                    }
                }
            }

            // Language Section
            item {
                var showLanguageDialog by remember { mutableStateOf(false) }

                SettingsSection(title = "Language") {
                    SettingsItem(
                        icon = Icons.Default.Settings,
                        title = "App Language",
                        subtitle = SettingsLanguage.entries.find { it.code == uiState.selectedLanguage }?.displayName ?: "English",
                        trailing = {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        onClick = { showLanguageDialog = true }
                    )
                }

                if (showLanguageDialog) {
                    LanguageSelectionDialog(
                        currentLanguage = uiState.selectedLanguage,
                        onLanguageSelected = { language ->
                            viewModel.setLanguage(language)
                            showLanguageDialog = false
                        },
                        onDismiss = { showLanguageDialog = false }
                    )
                }
            }

            // App Info Section
            item {
                SettingsSection(title = "App Information") {
                    var showAboutDialog by remember { mutableStateOf(false) }
                    var showPrivacyDialog by remember { mutableStateOf(false) }

                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "About Score24Seven",
                        subtitle = "App version ${viewModel.getAppVersion()}",
                        onClick = { showAboutDialog = true }
                    )

                    SettingsItem(
                        icon = Icons.Default.Lock,
                        title = "Privacy Policy",
                        subtitle = "Learn how we protect your data",
                        onClick = { showPrivacyDialog = true }
                    )

                    SettingsItem(
                        icon = Icons.Default.Star,
                        title = "Rate the App",
                        subtitle = "Help us improve by rating on Play Store",
                        onClick = {
                            // TODO: Open Play Store
                        }
                    )

                    if (showAboutDialog) {
                        AboutDialog(onDismiss = { showAboutDialog = false })
                    }

                    if (showPrivacyDialog) {
                        PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
                    }
                }
            }

            // Data & Storage Section
            item {
                SettingsSection(title = "Data & Storage") {
                    SettingsItem(
                        icon = Icons.Default.Delete,
                        title = "Clear App Data",
                        subtitle = "Reset all settings and cached data",
                        titleColor = MaterialTheme.colorScheme.error,
                        onClick = viewModel::showClearDataDialog
                    )
                }
            }

            // Loading indicator
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Message display
            uiState.message?.let { message ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    // Clear Data Confirmation Dialog
    if (uiState.showClearDataDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideClearDataDialog,
            title = { Text("Clear All Data") },
            text = {
                Text("This will reset all your settings and clear cached data. This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = viewModel::clearAllData,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Clear Data")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideClearDataDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = titleColor,
                fontWeight = FontWeight.Medium
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        trailing?.invoke()
    }
}

@Composable
private fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Language") },
        text = {
            LazyColumn {
                items(SettingsLanguage.entries.size) { index ->
                    val language = SettingsLanguage.entries[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language.code) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLanguage == language.code,
                            onClick = { onLanguageSelected(language.code) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = language.displayName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("About Score24Seven")
            }
        },
        text = {
            Column {
                Text(
                    text = "Score24Seven",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your ultimate football companion for live scores, standings, fixtures, and more.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Features:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                val features = listOf(
                    "• Live match scores and updates",
                    "• Comprehensive league standings",
                    "• Fixture schedules and results",
                    "• Top scorers and statistics",
                    "• Multiple leagues and competitions",
                    "• Dark and light themes",
                    "• Push notifications"
                )

                features.forEach { feature ->
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "© 2025 Score24Seven. All rights reserved.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Privacy Policy")
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.height(400.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = "Data Collection",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Score24Seven collects minimal data necessary to provide our services:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val dataTypes = listOf(
                            "• App preferences and settings",
                            "• Anonymous usage analytics",
                            "• Crash reports for app improvement",
                            "• No personal information is collected"
                        )

                        dataTypes.forEach { type ->
                            Text(
                                text = type,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Data Usage",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your data is used solely to:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val usageTypes = listOf(
                            "• Improve app performance and stability",
                            "• Provide personalized experience",
                            "• Send relevant notifications (if enabled)",
                            "• Analyze app usage patterns"
                        )

                        usageTypes.forEach { usage ->
                            Text(
                                text = usage,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Data Protection",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "We implement industry-standard security measures to protect your data. Your information is never sold to third parties.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Contact Us",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "For privacy concerns or questions, contact us at privacy@score24seven.com",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Last updated: January 2025",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}