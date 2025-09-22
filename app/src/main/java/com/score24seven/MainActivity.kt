/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.score24seven.ui.design.theme.Score24SevenTheme
import com.score24seven.ui.navigation.Score24SevenNavigation
import com.score24seven.ui.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkMode by Score24SevenApplication.preferencesManager.isDarkMode.collectAsState()

            // Debug logging
            LaunchedEffect(isDarkMode) {
                println("🎨 DEBUG: MainActivity - Theme changed to: ${if (isDarkMode) "Dark" else "Light"}")
            }

            Score24SevenTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Score24SevenApp()
                }
            }
        }
    }
}

@Composable
fun Score24SevenApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Score24SevenBottomNavigation(
                navController = navController
            )
        }
    ) { paddingValues ->
        Score24SevenNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun Score24SevenBottomNavigation(
    navController: androidx.navigation.NavHostController
) {
    val items = listOf(
        BottomNavItem(
            title = "Home",
            route = Screen.Home.route,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            title = "Matches",
            route = Screen.Matches.route,
            icon = Icons.Default.DateRange
        ),
        BottomNavItem(
            title = "Live TV",
            route = Screen.LiveTV.route,
            icon = Icons.Default.PlayArrow
        ),
        BottomNavItem(
            title = "Leagues",
            route = Screen.Leagues.route,
            icon = Icons.Default.List
        ),
        BottomNavItem(
            title = "Settings",
            route = Screen.Settings.route,
            icon = Icons.Default.Settings
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)