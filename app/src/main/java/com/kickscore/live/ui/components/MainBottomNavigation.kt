/*
 * Modern Bottom Navigation Bar for KickScore Live
 * Inspired by FlashScore design with modern Material3 components
 */

package com.kickscore.live.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kickscore.live.ui.navigation.Screen

@Composable
fun MainBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val navigationItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Matches", Screen.Matches.route, Icons.Default.PlayArrow),
        BottomNavItem("Live TV", Screen.LiveTV.route, Icons.Default.PlayArrow),
        BottomNavItem("Leagues", Screen.Leagues.route, Icons.Default.Star),
        BottomNavItem("Settings", Screen.Settings.route, Icons.Default.Settings)
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}

private data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)