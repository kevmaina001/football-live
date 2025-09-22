/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.model

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class MatchTest {

    private val homeTeam = Team("1", "Arsenal", "arsenal.png", "ARS")
    private val awayTeam = Team("2", "Chelsea", "chelsea.png", "CHE")
    private val league = League("1", "Premier League", "pl.png", "England")

    @Test
    fun `getWinner returns home team when home score is higher`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 3,
            awayScore = 1,
            status = MatchStatus.FINISHED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When
        val winner = match.getWinner()

        // Then
        assertEquals(homeTeam, winner)
    }

    @Test
    fun `getWinner returns away team when away score is higher`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 1,
            awayScore = 3,
            status = MatchStatus.FINISHED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Stamford Bridge",
            isLive = false
        )

        // When
        val winner = match.getWinner()

        // Then
        assertEquals(awayTeam, winner)
    }

    @Test
    fun `getWinner returns null when scores are equal`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 2,
            awayScore = 2,
            status = MatchStatus.FINISHED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Wembley",
            isLive = false
        )

        // When
        val winner = match.getWinner()

        // Then
        assertNull(winner)
    }

    @Test
    fun `getWinner returns null when scores are not available`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = null,
            awayScore = null,
            status = MatchStatus.SCHEDULED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When
        val winner = match.getWinner()

        // Then
        assertNull(winner)
    }

    @Test
    fun `isDraw returns true when scores are equal and available`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 1,
            awayScore = 1,
            status = MatchStatus.FINISHED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When & Then
        assertTrue(match.isDraw())
    }

    @Test
    fun `isDraw returns false when scores are different`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 2,
            awayScore = 1,
            status = MatchStatus.FINISHED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When & Then
        assertFalse(match.isDraw())
    }

    @Test
    fun `isDraw returns false when scores are not available`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = null,
            awayScore = null,
            status = MatchStatus.SCHEDULED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When & Then
        assertFalse(match.isDraw())
    }

    @Test
    fun `getDisplayTime returns formatted time for scheduled match`() {
        // Given
        val startTime = LocalDateTime.of(2024, 1, 15, 15, 30)
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = null,
            awayScore = null,
            status = MatchStatus.SCHEDULED,
            startTime = startTime,
            league = league,
            minute = null,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When
        val displayTime = match.getDisplayTime()

        // Then
        assertEquals("15:30", displayTime)
    }

    @Test
    fun `getDisplayTime returns minute with apostrophe for live match`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 1,
            awayScore = 0,
            status = MatchStatus.LIVE,
            startTime = LocalDateTime.now().minusMinutes(30),
            league = league,
            minute = 67,
            venue = "Emirates Stadium",
            isLive = true
        )

        // When
        val displayTime = match.getDisplayTime()

        // Then
        assertEquals("67'", displayTime)
    }

    @Test
    fun `getDisplayTime returns FT for finished match`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 2,
            awayScore = 1,
            status = MatchStatus.FINISHED,
            startTime = LocalDateTime.now().minusHours(2),
            league = league,
            minute = 90,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When
        val displayTime = match.getDisplayTime()

        // Then
        assertEquals("FT", displayTime)
    }

    @Test
    fun `getDisplayTime returns HT for half time`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 1,
            awayScore = 0,
            status = MatchStatus.HALF_TIME,
            startTime = LocalDateTime.now().minusMinutes(45),
            league = league,
            minute = 45,
            venue = "Emirates Stadium",
            isLive = true
        )

        // When
        val displayTime = match.getDisplayTime()

        // Then
        assertEquals("HT", displayTime)
    }

    @Test
    fun `getScoreDisplay returns dash when scores not available`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = null,
            awayScore = null,
            status = MatchStatus.SCHEDULED,
            startTime = LocalDateTime.now(),
            league = league,
            minute = null,
            venue = "Emirates Stadium",
            isLive = false
        )

        // When
        val scoreDisplay = match.getScoreDisplay()

        // Then
        assertEquals("- : -", scoreDisplay)
    }

    @Test
    fun `getScoreDisplay returns actual scores when available`() {
        // Given
        val match = Match(
            id = "1",
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 3,
            awayScore = 1,
            status = MatchStatus.LIVE,
            startTime = LocalDateTime.now(),
            league = league,
            minute = 75,
            venue = "Emirates Stadium",
            isLive = true
        )

        // When
        val scoreDisplay = match.getScoreDisplay()

        // Then
        assertEquals("3 : 1", scoreDisplay)
    }
}