/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.mapper

import com.score24seven.data.database.entity.StandingEntity
import com.score24seven.data.dto.StandingDto
import com.score24seven.data.dto.StandingStatsDto
import com.score24seven.data.dto.StandingGoalsDto
import com.score24seven.data.dto.LeagueDto
import com.score24seven.data.api.TeamDto
import com.score24seven.domain.model.Standing
import com.score24seven.domain.model.StandingStats
import com.score24seven.domain.model.StandingGoals
import com.score24seven.domain.model.Team

object StandingMapper {

    fun mapEntityToDomain(entity: StandingEntity): Standing {
        return Standing(
            rank = entity.rank,
            team = Team(
                id = entity.teamId,
                name = entity.teamName,
                logo = entity.teamLogo
            ),
            points = entity.points,
            goalsDiff = entity.goalsDiff,
            form = entity.form,
            status = entity.status,
            description = entity.description,
            all = StandingStats(
                played = entity.played,
                win = entity.wins,
                draw = entity.draws,
                lose = entity.losses,
                goals = StandingGoals(
                    goalsFor = entity.goalsFor,
                    goalsAgainst = entity.goalsAgainst
                )
            ),
            home = StandingStats(
                played = entity.homePlayed,
                win = entity.homeWins,
                draw = entity.homeDraws,
                lose = entity.homeLosses,
                goals = StandingGoals(
                    goalsFor = entity.homeGoalsFor,
                    goalsAgainst = entity.homeGoalsAgainst
                )
            ),
            away = StandingStats(
                played = entity.awayPlayed,
                win = entity.awayWins,
                draw = entity.awayDraws,
                lose = entity.awayLosses,
                goals = StandingGoals(
                    goalsFor = entity.awayGoalsFor,
                    goalsAgainst = entity.awayGoalsAgainst
                )
            ),
            group = entity.group
        )
    }

    fun mapEntitiesToDomain(entities: List<StandingEntity>): List<Standing> {
        return entities.map { mapEntityToDomain(it) }
    }

    fun mapDtoToDomain(dto: StandingDto, league: LeagueDto? = null): Standing {
        return Standing(
            rank = dto.rank,
            team = Team(
                id = dto.team.id,
                name = dto.team.name,
                logo = dto.team.logo
            ),
            points = dto.points,
            goalsDiff = dto.goalsDiff,
            form = dto.form,
            status = dto.status,
            description = dto.description,
            update = dto.update,
            all = mapStatsDto(dto.all, dto.goalsDiff),
            home = mapStatsDto(dto.home),
            away = mapStatsDto(dto.away),
            group = dto.group
        )
    }

    private fun mapStatsDto(statsDto: StandingStatsDto, goalsDiff: Int = 0): StandingStats {
        return StandingStats(
            played = statsDto.played,
            win = statsDto.win,
            draw = statsDto.draw,
            lose = statsDto.lose,
            goals = StandingGoals(
                goalsFor = statsDto.goals.goalsAgainst + goalsDiff, // Calculate goalsFor from goalsAgainst + goalsDiff
                goalsAgainst = statsDto.goals.goalsAgainst
            )
        )
    }

    fun mapDomainToEntity(standing: Standing, leagueId: Int, season: Int): StandingEntity {
        return StandingEntity(
            leagueId = leagueId,
            season = season,
            rank = standing.rank,
            teamId = standing.team.id,
            teamName = standing.team.name,
            teamLogo = standing.team.logo,
            points = standing.points,
            goalsDiff = standing.goalsDiff,
            form = standing.form,
            status = standing.status,
            description = standing.description,
            played = standing.all.played,
            wins = standing.all.win,
            draws = standing.all.draw,
            losses = standing.all.lose,
            goalsFor = standing.all.goals.goalsFor,
            goalsAgainst = standing.all.goals.goalsAgainst,
            homePlayed = standing.home?.played ?: 0,
            homeWins = standing.home?.win ?: 0,
            homeDraws = standing.home?.draw ?: 0,
            homeLosses = standing.home?.lose ?: 0,
            homeGoalsFor = standing.home?.goals?.goalsFor ?: 0,
            homeGoalsAgainst = standing.home?.goals?.goalsAgainst ?: 0,
            awayPlayed = standing.away?.played ?: 0,
            awayWins = standing.away?.win ?: 0,
            awayDraws = standing.away?.draw ?: 0,
            awayLosses = standing.away?.lose ?: 0,
            awayGoalsFor = standing.away?.goals?.goalsFor ?: 0,
            awayGoalsAgainst = standing.away?.goals?.goalsAgainst ?: 0,
            group = standing.group
        )
    }

    fun mapDomainToEntities(standings: List<Standing>, leagueId: Int, season: Int): List<StandingEntity> {
        return standings.map { mapDomainToEntity(it, leagueId, season) }
    }
}