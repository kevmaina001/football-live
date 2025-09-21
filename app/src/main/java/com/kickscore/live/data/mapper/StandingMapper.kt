/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.database.entity.StandingEntity
import com.kickscore.live.domain.model.Standing
import com.kickscore.live.domain.model.StandingStats
import com.kickscore.live.domain.model.StandingGoals
import com.kickscore.live.domain.model.Team

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
}