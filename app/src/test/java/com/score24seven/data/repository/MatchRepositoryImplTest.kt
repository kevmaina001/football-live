/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.repository

import com.score24seven.data.database.dao.MatchDao
import com.score24seven.data.database.entity.MatchEntity
import com.score24seven.data.mapper.MatchMapper
import com.score24seven.data.network.FootballApiService
import com.score24seven.data.network.dto.MatchDto
import com.score24seven.data.network.dto.TeamDto
import com.score24seven.data.websocket.WebSocketClient
import com.score24seven.domain.model.League
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.MatchStatus
import com.score24seven.domain.model.Team
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class MatchRepositoryImplTest {

    private lateinit var repository: MatchRepositoryImpl
    private val mockApiService = mockk<FootballApiService>()
    private val mockMatchDao = mockk<MatchDao>()
    private val mockWebSocketClient = mockk<WebSocketClient>()
    private val mockMatchMapper = mockk<MatchMapper>()

    private val testMatchDto = MatchDto(
        id = "1",
        homeTeam = TeamDto("1", "Arsenal", "arsenal.png", "ARS"),
        awayTeam = TeamDto("2", "Chelsea", "chelsea.png", "CHE"),
        homeScore = 2,
        awayScore = 1,
        status = "LIVE",
        startTime = "2024-01-15T15:00:00",
        leagueId = "1",
        leagueName = "Premier League",
        leagueLogo = "pl.png",
        leagueCountry = "England",
        minute = 67,
        venue = "Emirates Stadium"
    )

    private val testMatchEntity = MatchEntity(
        id = "1",
        homeTeamId = "1",
        homeTeamName = "Arsenal",
        homeTeamLogo = "arsenal.png",
        homeTeamCode = "ARS",
        awayTeamId = "2",
        awayTeamName = "Chelsea",
        awayTeamLogo = "chelsea.png",
        awayTeamCode = "CHE",
        homeScore = 2,
        awayScore = 1,
        status = "LIVE",
        startTime = LocalDateTime.of(2024, 1, 15, 15, 0),
        leagueId = "1",
        leagueName = "Premier League",
        leagueLogo = "pl.png",
        leagueCountry = "England",
        minute = 67,
        venue = "Emirates Stadium",
        isLive = true,
        lastUpdated = LocalDateTime.now()
    )

    private val testMatch = Match(
        id = "1",
        homeTeam = Team("1", "Arsenal", "arsenal.png", "ARS"),
        awayTeam = Team("2", "Chelsea", "chelsea.png", "CHE"),
        homeScore = 2,
        awayScore = 1,
        status = MatchStatus.LIVE,
        startTime = LocalDateTime.of(2024, 1, 15, 15, 0),
        league = League("1", "Premier League", "pl.png", "England"),
        minute = 67,
        venue = "Emirates Stadium",
        isLive = true
    )

    @Before
    fun setup() {
        repository = MatchRepositoryImpl(
            apiService = mockApiService,
            matchDao = mockMatchDao,
            webSocketClient = mockWebSocketClient,
            matchMapper = mockMatchMapper
        )
    }

    @Test
    fun `getLiveMatches returns cached data when available`() = runBlocking {
        // Given
        every { mockMatchDao.getLiveMatches() } returns flowOf(listOf(testMatchEntity))
        every { mockMatchMapper.mapToDomain(testMatchEntity) } returns testMatch

        // When
        val result = repository.getLiveMatches().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].size)
        assertEquals(testMatch, result[0][0])
        verify { mockMatchDao.getLiveMatches() }
        verify { mockMatchMapper.mapToDomain(testMatchEntity) }
    }

    @Test
    fun `getLiveMatches fetches from API when cache is empty`() = runBlocking {
        // Given
        every { mockMatchDao.getLiveMatches() } returns flowOf(emptyList())
        coEvery { mockApiService.getLiveMatches() } returns listOf(testMatchDto)
        every { mockMatchMapper.mapToEntity(testMatchDto) } returns testMatchEntity
        every { mockMatchMapper.mapToDomain(testMatchEntity) } returns testMatch
        coEvery { mockMatchDao.insertMatches(any()) } returns Unit

        // When
        val result = repository.getLiveMatches().toList()

        // Then
        assertEquals(1, result.size)
        coVerify { mockApiService.getLiveMatches() }
        coVerify { mockMatchDao.insertMatches(listOf(testMatchEntity)) }
    }

    @Test
    fun `refreshMatches updates cache with fresh data`() = runBlocking {
        // Given
        coEvery { mockApiService.getLiveMatches() } returns listOf(testMatchDto)
        every { mockMatchMapper.mapToEntity(testMatchDto) } returns testMatchEntity
        coEvery { mockMatchDao.insertMatches(any()) } returns Unit

        // When
        repository.refreshMatches()

        // Then
        coVerify { mockApiService.getLiveMatches() }
        coVerify { mockMatchDao.insertMatches(listOf(testMatchEntity)) }
    }

    @Test
    fun `getTodayMatches returns today's matches from cache`() = runBlocking {
        // Given
        val today = LocalDateTime.now().toLocalDate()
        every { mockMatchDao.getMatchesByDate(today) } returns flowOf(listOf(testMatchEntity))
        every { mockMatchMapper.mapToDomain(testMatchEntity) } returns testMatch

        // When
        val result = repository.getTodayMatches().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].size)
        assertEquals(testMatch, result[0][0])
        verify { mockMatchDao.getMatchesByDate(today) }
    }

    @Test
    fun `getMatchDetails returns match details from API`() = runBlocking {
        // Given
        val matchId = "1"
        coEvery { mockApiService.getMatchDetails(matchId) } returns testMatchDto
        every { mockMatchMapper.mapToEntity(testMatchDto) } returns testMatchEntity
        every { mockMatchMapper.mapToDomain(testMatchEntity) } returns testMatch
        coEvery { mockMatchDao.insertMatches(any()) } returns Unit

        // When
        val result = repository.getMatchDetails(matchId).toList()

        // Then
        assertEquals(1, result.size)
        coVerify { mockApiService.getMatchDetails(matchId) }
    }
}