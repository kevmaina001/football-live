/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.database.entity.MatchEntity
import com.kickscore.live.data.dto.MatchDto
import com.kickscore.live.data.websocket.LiveMatchUpdate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object MatchMapper {

    fun mapDtoToEntity(dto: MatchDto): MatchEntity {
        return MatchEntity(
            id = dto.fixtureId,

            // Teams
            homeTeamId = dto.teams.home.id,
            homeTeamName = dto.teams.home.name,
            homeTeamLogo = dto.teams.home.logo,
            awayTeamId = dto.teams.away.id,
            awayTeamName = dto.teams.away.name,
            awayTeamLogo = dto.teams.away.logo,

            // League
            leagueId = dto.league.id,
            leagueName = dto.league.name,
            leagueLogo = dto.league.logo,
            leagueCountry = dto.league.country,
            season = dto.league.season,
            round = dto.league.round,

            // Timing
            matchDateTime = parseDateTime(dto.fixture.date),
            timezone = dto.fixture.timezone,
            timestamp = dto.fixture.timestamp,

            // Status
            status = dto.fixture.status.short,
            statusLong = dto.fixture.status.long,
            elapsed = dto.fixture.status.elapsed,

            // Scores
            homeScore = dto.goals.home,
            awayScore = dto.goals.away,
            halftimeHome = dto.score.halftime.home,
            halftimeAway = dto.score.halftime.away,
            fulltimeHome = dto.score.fulltime.home,
            fulltimeAway = dto.score.fulltime.away,

            // Venue
            venueId = dto.fixture.venue?.id,
            venueName = dto.fixture.venue?.name,
            venueCity = dto.fixture.venue?.city,

            // Additional
            referee = dto.fixture.referee,
            winner = determineWinner(dto.teams.home.winner, dto.teams.away.winner),

            // Metadata
            isLive = isMatchLive(dto.fixture.status.short),
            hasEvents = !dto.events.isNullOrEmpty(),
            hasLineups = !dto.lineups.isNullOrEmpty(),
            hasStatistics = !dto.statistics.isNullOrEmpty()
        )
    }

    fun mapDtosToEntities(dtos: List<MatchDto>): List<MatchEntity> {
        return dtos.map { mapDtoToEntity(it) }
    }

    fun updateEntityWithLiveData(entity: MatchEntity, liveUpdate: LiveMatchUpdate): MatchEntity {
        return entity.copy(
            homeScore = liveUpdate.homeScore ?: entity.homeScore,
            awayScore = liveUpdate.awayScore ?: entity.awayScore,
            status = liveUpdate.status,
            elapsed = liveUpdate.elapsed,
            isLive = isMatchLive(liveUpdate.status),
            lastUpdated = LocalDateTime.now(),
            winner = determineWinnerFromScores(
                liveUpdate.homeScore,
                liveUpdate.awayScore,
                liveUpdate.status
            )
        )
    }

    private fun parseDateTime(dateString: String): LocalDateTime {
        return try {
            // Try parsing ISO 8601 format
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val zonedDateTime = java.time.ZonedDateTime.parse(dateString, formatter)
            zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        } catch (e: Exception) {
            // Fallback to current time if parsing fails
            LocalDateTime.now()
        }
    }

    private fun isMatchLive(status: String): Boolean {
        return status.uppercase() in listOf("LIVE", "1H", "2H", "HT", "ET", "BT", "P")
    }

    private fun determineWinner(homeWinner: Boolean?, awayWinner: Boolean?): String? {
        return when {
            homeWinner == true -> "home"
            awayWinner == true -> "away"
            homeWinner == false && awayWinner == false -> "draw"
            else -> null
        }
    }

    private fun determineWinnerFromScores(
        homeScore: Int?,
        awayScore: Int?,
        status: String
    ): String? {
        if (homeScore == null || awayScore == null) return null
        if (!isMatchFinished(status)) return null

        return when {
            homeScore > awayScore -> "home"
            awayScore > homeScore -> "away"
            else -> "draw"
        }
    }

    private fun isMatchFinished(status: String): Boolean {
        return status.uppercase() in listOf("FT", "AET", "PEN", "FT_PEN")
    }
}