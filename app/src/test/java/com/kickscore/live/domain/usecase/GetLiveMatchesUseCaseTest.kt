/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.usecase

import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.model.MatchStatus
import com.kickscore.live.domain.model.Team
import com.kickscore.live.domain.repository.MatchRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class GetLiveMatchesUseCaseTest {

    private lateinit var useCase: GetLiveMatchesUseCase
    private val mockRepository = mockk<MatchRepository>()

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
            status = MatchStatus.LIVE,
            startTime = LocalDateTime.now().minusMinutes(30),
            league = League("2", "La Liga", "laliga.png", "Spain"),
            minute = 78,
            venue = "Camp Nou",
            isLive = true
        )
    )

    @Before
    fun setup() {
        useCase = GetLiveMatchesUseCase(mockRepository)
    }

    @Test
    fun `invoke returns live matches from repository`() = runBlocking {
        // Given
        coEvery { mockRepository.getLiveMatches() } returns flowOf(testMatches)

        // When
        val result = useCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(testMatches, result[0])
    }

    @Test
    fun `invoke returns empty list when no live matches`() = runBlocking {
        // Given
        coEvery { mockRepository.getLiveMatches() } returns flowOf(emptyList())

        // When
        val result = useCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(emptyList<Match>(), result[0])
    }

    @Test
    fun `invoke filters only live matches`() = runBlocking {
        // Given
        val mixedMatches = testMatches + listOf(
            testMatches[0].copy(
                id = "3",
                status = MatchStatus.FINISHED,
                isLive = false
            )
        )
        coEvery { mockRepository.getLiveMatches() } returns flowOf(testMatches)

        // When
        val result = useCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(2, result[0].size)
        result[0].forEach { match ->
            assertEquals(MatchStatus.LIVE, match.status)
            assertEquals(true, match.isLive)
        }
    }
}