/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.database.entity.MatchEntity
import com.kickscore.live.data.network.dto.MatchDto
import com.kickscore.live.data.network.dto.TeamDto
import com.kickscore.live.domain.model.MatchStatus
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class MatchMapperTest {

    private lateinit var mapper: MatchMapper

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

    @Before
    fun setup() {
        mapper = MatchMapper()
    }

    @Test
    fun `mapToEntity converts MatchDto to MatchEntity correctly`() {
        // When
        val result = mapper.mapToEntity(testMatchDto)

        // Then
        assertEquals(testMatchDto.id, result.id)
        assertEquals(testMatchDto.homeTeam.id, result.homeTeamId)
        assertEquals(testMatchDto.homeTeam.name, result.homeTeamName)
        assertEquals(testMatchDto.homeTeam.logo, result.homeTeamLogo)
        assertEquals(testMatchDto.homeTeam.code, result.homeTeamCode)
        assertEquals(testMatchDto.awayTeam.id, result.awayTeamId)
        assertEquals(testMatchDto.awayTeam.name, result.awayTeamName)
        assertEquals(testMatchDto.awayTeam.logo, result.awayTeamLogo)
        assertEquals(testMatchDto.awayTeam.code, result.awayTeamCode)
        assertEquals(testMatchDto.homeScore, result.homeScore)
        assertEquals(testMatchDto.awayScore, result.awayScore)
        assertEquals(testMatchDto.status, result.status)
        assertEquals(LocalDateTime.of(2024, 1, 15, 15, 0), result.startTime)
        assertEquals(testMatchDto.leagueId, result.leagueId)
        assertEquals(testMatchDto.leagueName, result.leagueName)
        assertEquals(testMatchDto.leagueLogo, result.leagueLogo)
        assertEquals(testMatchDto.leagueCountry, result.leagueCountry)
        assertEquals(testMatchDto.minute, result.minute)
        assertEquals(testMatchDto.venue, result.venue)
        assertEquals(testMatchDto.status == "LIVE", result.isLive)
    }

    @Test
    fun `mapToDomain converts MatchEntity to Domain Match correctly`() {
        // When
        val result = mapper.mapToDomain(testMatchEntity)

        // Then
        assertEquals(testMatchEntity.id, result.id)
        assertEquals(testMatchEntity.homeTeamId, result.homeTeam.id)
        assertEquals(testMatchEntity.homeTeamName, result.homeTeam.name)
        assertEquals(testMatchEntity.homeTeamLogo, result.homeTeam.logo)
        assertEquals(testMatchEntity.homeTeamCode, result.homeTeam.code)
        assertEquals(testMatchEntity.awayTeamId, result.awayTeam.id)
        assertEquals(testMatchEntity.awayTeamName, result.awayTeam.name)
        assertEquals(testMatchEntity.awayTeamLogo, result.awayTeam.logo)
        assertEquals(testMatchEntity.awayTeamCode, result.awayTeam.code)
        assertEquals(testMatchEntity.homeScore, result.homeScore)
        assertEquals(testMatchEntity.awayScore, result.awayScore)
        assertEquals(MatchStatus.LIVE, result.status)
        assertEquals(testMatchEntity.startTime, result.startTime)
        assertEquals(testMatchEntity.leagueId, result.league.id)
        assertEquals(testMatchEntity.leagueName, result.league.name)
        assertEquals(testMatchEntity.leagueLogo, result.league.logo)
        assertEquals(testMatchEntity.leagueCountry, result.league.country)
        assertEquals(testMatchEntity.minute, result.minute)
        assertEquals(testMatchEntity.venue, result.venue)
        assertEquals(testMatchEntity.isLive, result.isLive)
    }

    @Test
    fun `mapStatusToDomain handles all status types`() {
        // Test all status mappings
        assertEquals(MatchStatus.SCHEDULED, mapper.mapStatusToDomain("SCHEDULED"))
        assertEquals(MatchStatus.LIVE, mapper.mapStatusToDomain("LIVE"))
        assertEquals(MatchStatus.HALF_TIME, mapper.mapStatusToDomain("HALF_TIME"))
        assertEquals(MatchStatus.FINISHED, mapper.mapStatusToDomain("FINISHED"))
        assertEquals(MatchStatus.POSTPONED, mapper.mapStatusToDomain("POSTPONED"))
        assertEquals(MatchStatus.CANCELLED, mapper.mapStatusToDomain("CANCELLED"))
        assertEquals(MatchStatus.SCHEDULED, mapper.mapStatusToDomain("UNKNOWN_STATUS"))
    }

    @Test
    fun `mapToEntity handles null minute correctly`() {
        // Given
        val dtoWithNullMinute = testMatchDto.copy(minute = null)

        // When
        val result = mapper.mapToEntity(dtoWithNullMinute)

        // Then
        assertEquals(null, result.minute)
    }

    @Test
    fun `mapToEntity sets isLive correctly for different statuses`() {
        // Test LIVE status
        val liveDto = testMatchDto.copy(status = "LIVE")
        assertEquals(true, mapper.mapToEntity(liveDto).isLive)

        // Test HALF_TIME status
        val halfTimeDto = testMatchDto.copy(status = "HALF_TIME")
        assertEquals(true, mapper.mapToEntity(halfTimeDto).isLive)

        // Test FINISHED status
        val finishedDto = testMatchDto.copy(status = "FINISHED")
        assertEquals(false, mapper.mapToEntity(finishedDto).isLive)

        // Test SCHEDULED status
        val scheduledDto = testMatchDto.copy(status = "SCHEDULED")
        assertEquals(false, mapper.mapToEntity(scheduledDto).isLive)
    }

    @Test
    fun `mapToDomain handles null scores correctly`() {
        // Given
        val entityWithNullScores = testMatchEntity.copy(
            homeScore = null,
            awayScore = null
        )

        // When
        val result = mapper.mapToDomain(entityWithNullScores)

        // Then
        assertEquals(null, result.homeScore)
        assertEquals(null, result.awayScore)
    }
}