/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.dto.TopScorerDto
import com.kickscore.live.data.dto.TopScorerPlayerDto
import com.kickscore.live.data.dto.PlayerBirthDto
import com.kickscore.live.data.dto.PlayerStatisticsDto
import com.kickscore.live.data.dto.PlayerGamesDto
import com.kickscore.live.data.dto.PlayerGoalsDto
import com.kickscore.live.data.dto.PlayerSubstitutesDto
import com.kickscore.live.data.dto.PlayerShotsDto
import com.kickscore.live.data.dto.PlayerPassesDto
import com.kickscore.live.data.dto.PlayerTacklesDto
import com.kickscore.live.data.dto.PlayerDuelsDto
import com.kickscore.live.data.dto.PlayerDribblesDto
import com.kickscore.live.data.dto.PlayerFoulsDto
import com.kickscore.live.data.dto.PlayerCardsDto
import com.kickscore.live.data.dto.PlayerPenaltyDto
import com.kickscore.live.domain.model.TopScorer
import com.kickscore.live.domain.model.TopScorerPlayer
import com.kickscore.live.domain.model.PlayerBirth
import com.kickscore.live.domain.model.PlayerStatistics
import com.kickscore.live.domain.model.PlayerGames
import com.kickscore.live.domain.model.PlayerGoals
import com.kickscore.live.domain.model.PlayerSubstitutes
import com.kickscore.live.domain.model.PlayerShots
import com.kickscore.live.domain.model.PlayerPasses
import com.kickscore.live.domain.model.PlayerTackles
import com.kickscore.live.domain.model.PlayerDuels
import com.kickscore.live.domain.model.PlayerDribbles
import com.kickscore.live.domain.model.PlayerFouls
import com.kickscore.live.domain.model.PlayerCards
import com.kickscore.live.domain.model.PlayerPenalty
import com.kickscore.live.domain.model.Team
import com.kickscore.live.domain.model.League

object TopScorerMapper {

    fun mapDtoToDomain(dto: TopScorerDto): TopScorer {
        return TopScorer(
            player = mapPlayerDtoToDomain(dto.player),
            statistics = dto.statistics.map { mapPlayerStatisticsDtoToDomain(it) }
        )
    }

    fun mapPlayerDtosToDomain(playerDtos: List<com.kickscore.live.data.api.PlayerDto>): List<TopScorer> {
        return playerDtos.map { playerDto ->
            TopScorer(
                player = TopScorerPlayer(
                    id = playerDto.id,
                    name = playerDto.name,
                    firstname = playerDto.firstname,
                    lastname = playerDto.lastname,
                    age = playerDto.age,
                    birth = playerDto.birth?.let { birth ->
                        PlayerBirth(
                            date = birth.date,
                            place = birth.place,
                            country = birth.country
                        )
                    },
                    nationality = playerDto.nationality,
                    height = playerDto.height,
                    weight = playerDto.weight,
                    injured = playerDto.injured ?: false,
                    photo = playerDto.photo
                ),
                statistics = playerDto.statistics?.map { stat ->
                    PlayerStatistics(
                        team = Team(
                            id = stat.team.id,
                            name = stat.team.name,
                            logo = stat.team.logo
                        ),
                        league = League(
                            id = stat.league.id,
                            name = stat.league.name,
                            country = stat.league.country,
                            logo = stat.league.logo,
                            season = stat.league.season,
                            round = null
                        ),
                        games = PlayerGames(
                            appearences = stat.games.appearences,
                            lineups = stat.games.lineups,
                            minutes = stat.games.minutes,
                            number = stat.games.number,
                            position = stat.games.position,
                            rating = stat.games.rating,
                            captain = stat.games.captain ?: false
                        ),
                        substitutes = null,
                        shots = null,
                        goals = PlayerGoals(
                            total = stat.goals.total,
                            conceded = stat.goals.conceded,
                            assists = stat.goals.assists,
                            saves = stat.goals.saves
                        ),
                        passes = null,
                        tackles = null,
                        duels = null,
                        dribbles = null,
                        fouls = null,
                        cards = null,
                        penalty = null
                    )
                } ?: emptyList()
            )
        }
    }

    fun mapDtosToDomain(dtos: List<TopScorerDto>): List<TopScorer> {
        return dtos.map { mapDtoToDomain(it) }
    }

    private fun mapPlayerDtoToDomain(dto: TopScorerPlayerDto): TopScorerPlayer {
        return TopScorerPlayer(
            id = dto.id,
            name = dto.name,
            firstname = dto.firstname,
            lastname = dto.lastname,
            age = dto.age,
            birth = dto.birth?.let { mapPlayerBirthDtoToDomain(it) },
            nationality = dto.nationality,
            height = dto.height,
            weight = dto.weight,
            injured = dto.injured,
            photo = dto.photo
        )
    }

    private fun mapPlayerBirthDtoToDomain(dto: PlayerBirthDto): PlayerBirth {
        return PlayerBirth(
            date = dto.date,
            place = dto.place,
            country = dto.country
        )
    }

    private fun mapPlayerStatisticsDtoToDomain(dto: PlayerStatisticsDto): PlayerStatistics {
        return PlayerStatistics(
            team = Team(
                id = dto.team.id,
                name = dto.team.name,
                logo = dto.team.logo ?: ""
            ),
            league = League(
                id = dto.league.id,
                name = dto.league.name,
                country = dto.league.country,
                logo = dto.league.logo,
                flag = dto.league.flag,
                season = dto.league.season ?: 0
            ),
            games = mapPlayerGamesDtoToDomain(dto.games),
            substitutes = dto.substitutes?.let { mapPlayerSubstitutesDtoToDomain(it) },
            shots = dto.shots?.let { mapPlayerShotsDtoToDomain(it) },
            goals = mapPlayerGoalsDtoToDomain(dto.goals),
            passes = dto.passes?.let { mapPlayerPassesDtoToDomain(it) },
            tackles = dto.tackles?.let { mapPlayerTacklesDtoToDomain(it) },
            duels = dto.duels?.let { mapPlayerDuelsDtoToDomain(it) },
            dribbles = dto.dribbles?.let { mapPlayerDribblesDtoToDomain(it) },
            fouls = dto.fouls?.let { mapPlayerFoulsDtoToDomain(it) },
            cards = dto.cards?.let { mapPlayerCardsDtoToDomain(it) },
            penalty = dto.penalty?.let { mapPlayerPenaltyDtoToDomain(it) }
        )
    }

    private fun mapPlayerGamesDtoToDomain(dto: PlayerGamesDto): PlayerGames {
        return PlayerGames(
            appearences = dto.appearences,
            lineups = dto.lineups,
            minutes = dto.minutes,
            number = dto.number,
            position = dto.position,
            rating = dto.rating,
            captain = dto.captain
        )
    }

    private fun mapPlayerSubstitutesDtoToDomain(dto: PlayerSubstitutesDto): PlayerSubstitutes {
        return PlayerSubstitutes(
            substitutesIn = dto.substitutesIn,
            substitutesOut = dto.substitutesOut,
            bench = dto.bench
        )
    }

    private fun mapPlayerShotsDtoToDomain(dto: PlayerShotsDto): PlayerShots {
        return PlayerShots(
            total = dto.total,
            on = dto.on
        )
    }

    private fun mapPlayerGoalsDtoToDomain(dto: PlayerGoalsDto): PlayerGoals {
        return PlayerGoals(
            total = dto.total,
            conceded = dto.conceded,
            assists = dto.assists,
            saves = dto.saves
        )
    }

    private fun mapPlayerPassesDtoToDomain(dto: PlayerPassesDto): PlayerPasses {
        return PlayerPasses(
            total = dto.total,
            key = dto.key,
            accuracy = dto.accuracy
        )
    }

    private fun mapPlayerTacklesDtoToDomain(dto: PlayerTacklesDto): PlayerTackles {
        return PlayerTackles(
            total = dto.total,
            blocks = dto.blocks,
            interceptions = dto.interceptions
        )
    }

    private fun mapPlayerDuelsDtoToDomain(dto: PlayerDuelsDto): PlayerDuels {
        return PlayerDuels(
            total = dto.total,
            won = dto.won
        )
    }

    private fun mapPlayerDribblesDtoToDomain(dto: PlayerDribblesDto): PlayerDribbles {
        return PlayerDribbles(
            attempts = dto.attempts,
            success = dto.success,
            past = dto.past
        )
    }

    private fun mapPlayerFoulsDtoToDomain(dto: PlayerFoulsDto): PlayerFouls {
        return PlayerFouls(
            drawn = dto.drawn,
            committed = dto.committed
        )
    }

    private fun mapPlayerCardsDtoToDomain(dto: PlayerCardsDto): PlayerCards {
        return PlayerCards(
            yellow = dto.yellow,
            yellowred = dto.yellowred,
            red = dto.red
        )
    }

    private fun mapPlayerPenaltyDtoToDomain(dto: PlayerPenaltyDto): PlayerPenalty {
        return PlayerPenalty(
            won = dto.won,
            commited = dto.commited,
            scored = dto.scored,
            missed = dto.missed,
            saved = dto.saved
        )
    }
}