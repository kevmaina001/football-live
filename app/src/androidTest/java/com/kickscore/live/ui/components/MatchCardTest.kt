/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.model.MatchStatus
import com.kickscore.live.domain.model.Team
import com.kickscore.live.ui.design.theme.KickScoreTheme
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class MatchCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testMatch = Match(
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
    )

    @Test
    fun matchCard_displaysLiveMatch_correctly() {
        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = testMatch,
                    onClick = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chelsea").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 - 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("67'").assertIsDisplayed()
        composeTestRule.onNodeWithText("LIVE").assertIsDisplayed()
    }

    @Test
    fun matchCard_displaysScheduledMatch_correctly() {
        // Given
        val scheduledMatch = testMatch.copy(
            status = MatchStatus.SCHEDULED,
            homeScore = null,
            awayScore = null,
            minute = null,
            isLive = false,
            startTime = LocalDateTime.now().plusHours(2)
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = scheduledMatch,
                    onClick = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chelsea").assertIsDisplayed()
        composeTestRule.onNodeWithText("vs").assertIsDisplayed()
        // Time should be displayed for scheduled matches
    }

    @Test
    fun matchCard_displaysFinishedMatch_correctly() {
        // Given
        val finishedMatch = testMatch.copy(
            status = MatchStatus.FINISHED,
            minute = 90,
            isLive = false
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = finishedMatch,
                    onClick = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chelsea").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 - 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("FT").assertIsDisplayed()
    }

    @Test
    fun matchCard_handlesClick_correctly() {
        // Given
        var clickedMatchId: String? = null

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = testMatch,
                    onClick = { matchId -> clickedMatchId = matchId }
                )
            }
        }

        // Perform click
        composeTestRule.onNodeWithText("Arsenal").performClick()

        // Then
        assert(clickedMatchId == "1") { "Expected match ID '1' but got '$clickedMatchId'" }
    }

    @Test
    fun matchCard_displaysLeagueInfo() {
        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = testMatch,
                    onClick = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Premier League").assertIsDisplayed()
        composeTestRule.onNodeWithText("Emirates Stadium").assertIsDisplayed()
    }

    @Test
    fun matchCard_displaysTeamCodes() {
        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = testMatch,
                    onClick = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("ARS").assertIsDisplayed()
        composeTestRule.onNodeWithText("CHE").assertIsDisplayed()
    }

    @Test
    fun matchCard_displaysHalfTimeStatus() {
        // Given
        val halfTimeMatch = testMatch.copy(
            status = MatchStatus.HALF_TIME,
            minute = 45
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = halfTimeMatch,
                    onClick = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("HT").assertIsDisplayed()
    }

    @Test
    fun matchCard_displaysPostponedStatus() {
        // Given
        val postponedMatch = testMatch.copy(
            status = MatchStatus.POSTPONED,
            homeScore = null,
            awayScore = null,
            minute = null,
            isLive = false
        )

        // When
        composeTestRule.setContent {
            KickScoreTheme {
                MatchCard(
                    match = postponedMatch,
                    onClick = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("POSTPONED").assertIsDisplayed()
    }
}