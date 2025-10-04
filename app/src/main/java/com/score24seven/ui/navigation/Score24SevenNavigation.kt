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
import com.score24seven.ui.screen.NewMatchDetailScreen
import com.score24seven.ui.screen.MatchesScreen
import com.score24seven.ui.screen.LeaguesScreen
import com.score24seven.ui.screen.NewLeaguesScreen
import com.score24seven.ui.screen.ImprovedLeaguesScreen
import com.score24seven.ui.screen.SettingsScreen
import com.score24seven.ui.screen.LiveTvScreen
import com.score24seven.ui.screen.LeagueDetailsScreen
import com.score24seven.ui.screen.TeamDetailScreen
import com.score24seven.ui.screen.FavoritesScreen
import com.score24seven.ui.screen.SearchScreen

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
                },
                navController = navController
            )
        }

        composable(Screen.Matches.route) {
            MatchesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMatchDetail = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId))
                },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
            )
        }

        composable(Screen.LiveTV.route) {
            LiveTvScreen(
                onMatchClick = { match ->
                    navController.navigate(Screen.MatchDetail.createRoute(match.id))
                }
            )
        }

        composable(Screen.Leagues.route) {
            ImprovedLeaguesScreen(
                onNavigateToLeagueDetails = { leagueId, season, leagueName ->
                    navController.navigate(Screen.LeagueDetail.createRoute(leagueId, season, leagueName))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateToMatchDetail = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMatchDetail = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId))
                },
                onNavigateToTeamDetail = { teamId ->
                    navController.navigate(Screen.TeamDetail.createRoute(teamId))
                }
            )
        }

        composable(
            route = Screen.MatchDetail.route,
            arguments = listOf(
                navArgument("matchId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: return@composable
            NewMatchDetailScreen(
                matchId = matchId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTeamDetail = { teamId ->
                    navController.navigate(Screen.TeamDetail.createRoute(teamId))
                }
            )
        }

        composable(
            route = Screen.LeagueDetail.route,
            arguments = listOf(
                navArgument("leagueId") { type = NavType.IntType },
                navArgument("season") { type = NavType.IntType },
                navArgument("leagueName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val leagueId = backStackEntry.arguments?.getInt("leagueId") ?: return@composable
            val season = backStackEntry.arguments?.getInt("season") ?: return@composable
            val leagueName = backStackEntry.arguments?.getString("leagueName") ?: return@composable
            LeagueDetailsScreen(
                leagueId = leagueId,
                season = season,
                leagueName = leagueName.replace("-", "/"),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TeamDetail.route,
            arguments = listOf(
                navArgument("teamId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getInt("teamId") ?: return@composable
            TeamDetailScreen(
                teamId = teamId,
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
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object Search : Screen("search")
    object MatchDetail : Screen("match_detail/{matchId}") {
        fun createRoute(matchId: Int) = "match_detail/$matchId"
    }
    object LeagueDetail : Screen("league_detail/{leagueId}/{season}/{leagueName}") {
        fun createRoute(leagueId: Int, season: Int, leagueName: String) = "league_detail/$leagueId/$season/${leagueName.replace("/", "-")}"
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
        route = Screen.Favorites.route,
        title = "Favorites",
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
