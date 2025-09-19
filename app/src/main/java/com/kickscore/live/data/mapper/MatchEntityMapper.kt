/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.database.entity.MatchEntity
import com.kickscore.live.domain.model.Fixture
import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.model.MatchStatus
import com.kickscore.live.domain.model.Score
import com.kickscore.live.domain.model.ScorePeriod
import com.kickscore.live.domain.model.Team
import com.kickscore.live.domain.model.Venue

object MatchEntityMapper {

    fun mapEntityToDomain(entity: MatchEntity): Match {
        return Match(
            id = entity.id,
            homeTeam = Team(
                id = entity.homeTeamId,
                name = entity.homeTeamName,
                logo = entity.homeTeamLogo
            ),
            awayTeam = Team(
                id = entity.awayTeamId,
                name = entity.awayTeamName,
                logo = entity.awayTeamLogo
            ),
            league = League(
                id = entity.leagueId,
                name = entity.leagueName,
                country = entity.leagueCountry,
                logo = entity.leagueLogo,
                season = entity.season,
                round = entity.round
            ),
            fixture = Fixture(
                dateTime = entity.matchDateTime,
                timezone = entity.timezone,
                timestamp = entity.timestamp
            ),
            score = Score(
                home = entity.homeScore,
                away = entity.awayScore,
                halftime = if (entity.halftimeHome != null && entity.halftimeAway != null) {
                    ScorePeriod(entity.halftimeHome, entity.halftimeAway)
                } else null,
                fulltime = if (entity.fulltimeHome != null && entity.fulltimeAway != null) {
                    ScorePeriod(entity.fulltimeHome, entity.fulltimeAway)
                } else null
            ),
            status = MatchStatus(
                short = entity.status,
                long = entity.statusLong,
                elapsed = entity.elapsed,
                isLive = entity.isLive,
                isFinished = isMatchFinished(entity.status)
            ),
            venue = if (entity.venueId != null && entity.venueName != null) {
                Venue(
                    id = entity.venueId,
                    name = entity.venueName,
                    city = entity.venueCity
                )
            } else null,
            referee = entity.referee,
            isFavorite = entity.isFavorite,
            lastUpdated = entity.lastUpdated
        )
    }

    fun mapEntitiesToDomain(entities: List<MatchEntity>): List<Match> {
        return entities.map { mapEntityToDomain(it) }
    }

    fun mapDomainToEntity(match: Match): MatchEntity {
        return MatchEntity(
            id = match.id,
            homeTeamId = match.homeTeam.id,
            homeTeamName = match.homeTeam.name,
            homeTeamLogo = match.homeTeam.logo,
            awayTeamId = match.awayTeam.id,
            awayTeamName = match.awayTeam.name,
            awayTeamLogo = match.awayTeam.logo,
            leagueId = match.league.id,
            leagueName = match.league.name,
            leagueLogo = match.league.logo,
            leagueCountry = match.league.country,
            season = match.league.season,
            round = match.league.round,
            matchDateTime = match.fixture.dateTime,
            timezone = match.fixture.timezone,
            timestamp = match.fixture.timestamp,
            status = match.status.short,
            statusLong = match.status.long,
            elapsed = match.status.elapsed,
            isLive = match.status.isLive,
            homeScore = match.score.home,
            awayScore = match.score.away,
            halftimeHome = match.score.halftime?.home,
            halftimeAway = match.score.halftime?.away,
            fulltimeHome = match.score.fulltime?.home,
            fulltimeAway = match.score.fulltime?.away,
            venueId = match.venue?.id,
            venueName = match.venue?.name,
            venueCity = match.venue?.city,
            referee = match.referee,
            winner = null, // Will be calculated based on scores
            isFavorite = match.isFavorite,
            lastUpdated = match.lastUpdated,
            hasEvents = false,
            hasLineups = false,
            hasStatistics = false
        )
    }

    fun mapDomainToEntities(matches: List<Match>): List<MatchEntity> {
        return matches.map { mapDomainToEntity(it) }
    }

    private fun isMatchFinished(status: String): Boolean {
        return status.uppercase() in listOf("FT", "AET", "PEN", "FT_PEN", "CANC", "ABD", "PST")
    }
}