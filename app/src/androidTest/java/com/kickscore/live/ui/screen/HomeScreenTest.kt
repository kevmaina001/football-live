/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.model.MatchStatus
import com.kickscore.live.domain.model.Team
import com.kickscore.live.ui.design.theme.KickScoreTheme
import com.kickscore.live.ui.screen.home.HomeScreen
import com.kickscore.live.ui.screen.home.HomeScreenState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testMatches = listOf(
        Match(
            id = "1",
            homeTeam = Team("1", "Arsenal", "arsenal.png", "ARS"),
            awayTeam = Team("2", "Chelsea", "chelsea.png", "CHE"),
            homeScore = 2,
            awayScore = 1,
            status = MatchStatus.LIVE,
            startTime = LocalDateTime.now().minusMinutes(45),
            league = League("1", "Premier League", "pl.png", "England"),
            minute = 67,
            venue = "Emirates Stadium",
            isLive = true
        ),
        Match(
            id = "2",
            homeTeam = Team("3", "Barcelona", "barcelona.png", "BAR"),
            awayTeam = Team("4", "Real Madrid", "real.png", "RM"),
            homeScore = 1,
            awayScore = 3,
            status = MatchStatus.FINISHED,
            startTime = LocalDateTime.now().minusHours(2),
            league = League("2", "La Liga", "laliga.png", "Spain"),
            minute = 90,
            venue = "Camp Nou",
            isLive = false
        )
    )

    @Test
    fun homeScreen_displaysLiveMatches() {
        // Given
        val state = HomeScreenState(
            liveMatches = listOf(testMatches[0]),
            todayMatches = testMatches,
            isLoading = false
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                HomeScreen(
                    state = state,
                    onRefresh = { },
                    onMatchClick = { },
                    onLeagueFilter = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Live Matches").assertIsDisplayed()
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chelsea").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 - 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("67'").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysTodayMatches() {
        // Given
        val state = HomeScreenState(
            liveMatches = emptyList(),
            todayMatches = testMatches,
            isLoading = false
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                HomeScreen(
                    state = state,
                    onRefresh = { },
                    onMatchClick = { },
                    onLeagueFilter = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Today's Matches").assertIsDisplayed()
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Barcelona").assertIsDisplayed()
        composeTestRule.onNodeWithText("Real Madrid").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsLoadingState() {
        // Given
        val state = HomeScreenState(
            isLoading = true,
            liveMatches = emptyList(),
            todayMatches = emptyList()
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                HomeScreen(
                    state = state,
                    onRefresh = { },
                    onMatchClick = { },
                    onLeagueFilter = { }
                )
            }
        }

        // Then - Loading indicator should be displayed
        // Note: This would need to be implemented in the actual HomeScreen
        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsErrorState() {
        // Given
        val errorMessage = "Network error occurred"
        val state = HomeScreenState(
            error = errorMessage,
            isLoading = false,
            liveMatches = emptyList(),
            todayMatches = emptyList()
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                HomeScreen(
                    state = state,
                    onRefresh = { },
                    onMatchClick = { },
                    onLeagueFilter = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun homeScreen_handlesMatchClick() {
        // Given
        var clickedMatchId: String? = null
        val state = HomeScreenState(
            liveMatches = listOf(testMatches[0]),
            todayMatches = emptyList(),
            isLoading = false
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                HomeScreen(
                    state = state,
                    onRefresh = { },
                    onMatchClick = { matchId -> clickedMatchId = matchId },
                    onLeagueFilter = { }
                )
            }
        }

        // Perform click on match
        composeTestRule.onNodeWithText("Arsenal").performClick()

        // Then
        assert(clickedMatchId == "1") { "Expected match ID '1' but got '$clickedMatchId'" }
    }

    @Test
    fun homeScreen_showsEmptyState_whenNoMatches() {
        // Given
        val state = HomeScreenState(
            liveMatches = emptyList(),
            todayMatches = emptyList(),
            isLoading = false
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                HomeScreen(
                    state = state,
                    onRefresh = { },
                    onMatchClick = { },
                    onLeagueFilter = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("No matches available").assertIsDisplayed()
    }
}