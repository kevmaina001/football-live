/*
 * Navigation Fix - Quick patch for MatchesScreen date filtering
 * Replace the content of Screen.Matches.route in KickScoreNavigation.kt with this:
 */

/*
Add this import:
import com.kickscore.live.ui.screen.FixedMatchesScreen

Replace Screen.Matches.route composable with:
        composable(Screen.Matches.route) {
            FixedMatchesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMatchDetail = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId)) {
                        launchSingleTop = true
                    }
                }
            )
        }
*/