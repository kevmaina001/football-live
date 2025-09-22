/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.usecase

import com.score24seven.domain.model.League
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.MatchDetails
import com.score24seven.domain.model.MatchStatus
import com.score24seven.domain.model.Player
import com.score24seven.domain.model.Team
import com.score24seven.domain.repository.MatchRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class GetMatchDetailsUseCaseTest {

    private lateinit var useCase: GetMatchDetailsUseCase
    private val mockRepository = mockk<MatchRepository>()

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

    private val testMatchDetails = MatchDetails(
        match = testMatch,
        homeLineup = listOf(
            Player("1", "Bukayo Saka", 7, "Forward"),
            Player("2", "Martin Ødegaard", 8, "Midfielder")
        ),
        awayLineup = listOf(
            Player("3", "Mason Mount", 19, "Midfielder"),
            Player("4", "Nicolas Jackson", 15, "Forward")
        ),
        events = emptyList(),
        stats = emptyMap()
    )

    @Before
    fun setup() {
        useCase = GetMatchDetailsUseCase(mockRepository)
    }

    @Test
    fun `invoke returns match details for valid match id`() = runBlocking {
        // Given
        val matchId = "1"
        coEvery { mockRepository.getMatchDetails(matchId) } returns flowOf(testMatchDetails)

        // When
        val result = useCase(matchId).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(testMatchDetails, result[0])
        assertEquals(testMatch, result[0].match)
        assertEquals(2, result[0].homeLineup.size)
        assertEquals(2, result[0].awayLineup.size)
    }

    @Test
    fun `invoke handles repository error`() = runBlocking {
        // Given
        val matchId = "invalid"
        coEvery { mockRepository.getMatchDetails(matchId) } throws Exception("Match not found")

        // When & Then
        try {
            useCase(matchId).toList()
            assert(false) { "Expected exception was not thrown" }
        } catch (e: Exception) {
            assertEquals("Match not found", e.message)
        }
    }

    @Test
    fun `invoke returns match details with empty lineups when not available`() = runBlocking {
        // Given
        val matchId = "2"
        val detailsWithoutLineups = testMatchDetails.copy(
            homeLineup = emptyList(),
            awayLineup = emptyList()
        )
        coEvery { mockRepository.getMatchDetails(matchId) } returns flowOf(detailsWithoutLineups)

        // When
        val result = useCase(matchId).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(0, result[0].homeLineup.size)
        assertEquals(0, result[0].awayLineup.size)
        assertEquals(testMatch, result[0].match)
    }
}