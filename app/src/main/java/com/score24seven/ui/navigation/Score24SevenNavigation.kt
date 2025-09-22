/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.score24seven.ui.screen.HomeScreen
import com.score24seven.ui.screen.MatchDetailScreen
import com.score24seven.ui.screen.MatchesScreen
import com.score24seven.ui.screen.LeaguesScreen
import com.score24seven.ui.screen.NewLeaguesScreen
import com.score24seven.ui.screen.ImprovedLeaguesScreen
import com.score24seven.ui.screen.SettingsScreen
import com.score24seven.ui.screen.LiveTvScreen

@Composable
fun Score24SevenNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToMatchDetail = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId))
                }
            )
        }

        composable(Screen.Matches.route) {
            MatchesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMatchDetail = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId))
                }
            )
        }

        composable(Screen.LiveTV.route) {
            LiveTvScreen()
        }

        composable(Screen.Leagues.route) {
            ImprovedLeaguesScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.MatchDetail.route,
            arguments = listOf(
                navArgument("matchId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: return@composable
            MatchDetailScreen(
                matchId = matchId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Matches : Screen("matches")
    object LiveTV : Screen("live_tv")
    object Leagues : Screen("leagues")
    object Settings : Screen("settings")
    object MatchDetail : Screen("match_detail/{matchId}") {
        fun createRoute(matchId: Int) = "match_detail/$matchId"
    }
    object LeagueDetail : Screen("league_detail/{leagueId}") {
        fun createRoute(leagueId: Int) = "league_detail/$leagueId"
    }
    object TeamDetail : Screen("team_detail/{teamId}") {
        fun createRoute(teamId: Int) = "team_detail/$teamId"
    }
}

// Bottom navigation items
val bottomNavigationItems = listOf(
    BottomNavigationItem(
        route = Screen.Home.route,
        title = "Home",
        iconResId = 0, // Using material icons
        selectedIconResId = 0
    ),
    BottomNavigationItem(
        route = Screen.Matches.route,
        title = "Matches",
        iconResId = 0,
        selectedIconResId = 0
    ),
    BottomNavigationItem(
        route = Screen.LiveTV.route,
        title = "Live TV",
        iconResId = 0,
        selectedIconResId = 0
    ),
    BottomNavigationItem(
        route = Screen.Leagues.route,
        title = "Leagues",
        iconResId = 0,
        selectedIconResId = 0
    ),
    BottomNavigationItem(
        route = Screen.Settings.route,
        title = "Settings",
        iconResId = 0,
        selectedIconResId = 0
    )
)

data class BottomNavigationItem(
    val route: String,
    val title: String,
    val iconResId: Int,
    val selectedIconResId: Int
)