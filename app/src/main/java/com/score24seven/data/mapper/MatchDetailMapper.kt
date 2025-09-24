/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.mapper

import com.score24seven.data.database.entity.MatchEventEntity
import com.score24seven.data.database.entity.MatchLineupEntity
import com.score24seven.data.database.entity.MatchStatisticsEntity
import com.score24seven.data.dto.*
import com.score24seven.domain.model.*
import com.score24seven.data.api.EventDto as ApiEventDto
import com.score24seven.data.api.LineupDto as ApiLineupDto
import com.score24seven.data.api.StatisticsDto as ApiStatisticsDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Event mapping
fun EventDto.toEntity(matchId: Int): MatchEventEntity {
    return MatchEventEntity(
        matchId = matchId,
        timeElapsed = time.elapsed,
        timeExtra = time.extra,
        teamId = team.id,
        teamName = team.name,
        playerId = player.id,
        playerName = player.name,
        assistId = assist?.id,
        assistName = assist?.name,
        type = type,
        detail = detail,
        comments = comments
    )
}

fun MatchEventEntity.toDomain(): MatchEvent {
    return MatchEvent(
        time = EventTime(
            elapsed = timeElapsed,
            extra = timeExtra
        ),
        team = Team(
            id = teamId,
            name = teamName
        ),
        player = Player(
            id = playerId,
            name = playerName.takeIf { !it.isNullOrBlank() } ?: "Unknown Player"
        ),
        assist = assistId?.let {
            Player(
                id = it,
                name = (assistName ?: "").takeIf { it.isNotEmpty() } ?: "Unknown Player"
            )
        },
        type = EventType.fromString(type),
        detail = detail ?: "",
        comments = comments
    )
}

// Lineup mapping
fun LineupDto.toEntities(matchId: Int): List<MatchLineupEntity> {
    val entities = mutableListOf<MatchLineupEntity>()

    // Starting XI
    startXI.forEach { playerPos ->
        entities.add(
            MatchLineupEntity(
                matchId = matchId,
                teamId = team.id,
                teamName = team.name,
                formation = formation,
                coachId = coach.id,
                coachName = coach.name,
                coachPhoto = coach.photo,
                playerId = playerPos.player.id,
                playerName = playerPos.player.name,
                playerNumber = playerPos.player.number,
                playerPosition = playerPos.player.position,
                playerGrid = playerPos.player.grid,
                isStarting = true
            )
        )
    }

    // Substitutes
    substitutes.forEach { playerPos ->
        entities.add(
            MatchLineupEntity(
                matchId = matchId,
                teamId = team.id,
                teamName = team.name,
                formation = formation,
                coachId = coach.id,
                coachName = coach.name,
                coachPhoto = coach.photo,
                playerId = playerPos.player.id,
                playerName = playerPos.player.name,
                playerNumber = playerPos.player.number,
                playerPosition = playerPos.player.position,
                playerGrid = playerPos.player.grid,
                isStarting = false
            )
        )
    }

    return entities
}

fun List<MatchLineupEntity>.toDomainLineups(): List<Lineup> {
    return groupBy { it.teamId }.map { (_, entities) ->
        val firstEntity = entities.first()
        Lineup(
            team = Team(
                id = firstEntity.teamId,
                name = firstEntity.teamName
            ),
            formation = firstEntity.formation,
            coach = Coach(
                id = firstEntity.coachId,
                name = firstEntity.coachName,
                photo = firstEntity.coachPhoto
            ),
            startingEleven = entities.filter { it.isStarting }.map { entity ->
                LineupPlayer(
                    player = Player(
                        id = entity.playerId,
                        name = entity.playerName,
                        number = entity.playerNumber,
                        position = entity.playerPosition
                    ),
                    position = entity.playerPosition,
                    grid = entity.playerGrid
                )
            },
            substitutes = entities.filter { !it.isStarting }.map { entity ->
                LineupPlayer(
                    player = Player(
                        id = entity.playerId,
                        name = entity.playerName,
                        number = entity.playerNumber,
                        position = entity.playerPosition
                    ),
                    position = entity.playerPosition,
                    grid = entity.playerGrid
                )
            }
        )
    }
}

// Statistics mapping
fun StatisticsDto.toEntities(matchId: Int): List<MatchStatisticsEntity> {
    return statistics.map { stat ->
        MatchStatisticsEntity(
            matchId = matchId,
            teamId = team.id,
            teamName = team.name,
            type = stat.type,
            value = stat.value
        )
    }
}

fun List<MatchStatisticsEntity>.toDomainStatistics(): List<MatchStatistic> {
    // Group by type to combine home and away values
    return groupBy { it.type }.map { (type, entities) ->
        val homeEntity = entities.find { entity ->
            // This is a simplified approach - you might need to determine
            // home/away based on the match data
            entities.indexOf(entity) == 0
        }
        val awayEntity = entities.find { entity ->
            entities.indexOf(entity) == 1
        }

        MatchStatistic(
            type = type,
            homeValue = homeEntity?.value,
            awayValue = awayEntity?.value,
            homePercentage = homeEntity?.value?.let { parsePercentage(it) },
            awayPercentage = awayEntity?.value?.let { parsePercentage(it) }
        )
    }
}

private fun parsePercentage(value: String): Float? {
    return try {
        when {
            value.endsWith("%") -> value.dropLast(1).toFloat()
            value.contains("%") -> value.split("%")[0].toFloat()
            else -> value.toFloat()
        }
    } catch (e: NumberFormatException) {
        null
    }
}

// Helper functions for API to domain conversion
fun EventDto.toDomain(): MatchEvent {
    return MatchEvent(
        time = EventTime(
            elapsed = time.elapsed,
            extra = time.extra
        ),
        team = Team(
            id = team.id,
            name = team.name
        ),
        player = Player(
            id = player.id,
            name = player.name.takeIf { !it.isNullOrBlank() } ?: "Unknown Player"
        ),
        assist = assist?.let {
            Player(
                id = it.id,
                name = it.name.takeIf { !it.isNullOrBlank() } ?: "Unknown Player"
            )
        },
        type = EventType.fromString(type),
        detail = detail ?: "",
        comments = comments
    )
}

fun LineupDto.toDomain(): Lineup {
    return Lineup(
        team = Team(
            id = team.id,
            name = team.name
        ),
        formation = formation,
        coach = Coach(
            id = coach.id,
            name = coach.name,
            photo = coach.photo
        ),
        startingEleven = startXI.map { playerPos ->
            LineupPlayer(
                player = Player(
                    id = playerPos.player.id,
                    name = playerPos.player.name,
                    number = playerPos.player.number,
                    position = playerPos.player.position
                ),
                position = playerPos.player.position,
                grid = playerPos.player.grid
            )
        },
        substitutes = substitutes.map { playerPos ->
            LineupPlayer(
                player = Player(
                    id = playerPos.player.id,
                    name = playerPos.player.name,
                    number = playerPos.player.number,
                    position = playerPos.player.position
                ),
                position = playerPos.player.position,
                grid = playerPos.player.grid
            )
        }
    )
}

fun StatisticsDto.toDomain(otherTeamStats: StatisticsDto?): List<MatchStatistic> {
    return statistics.map { stat ->
        val otherTeamStat = otherTeamStats?.statistics?.find { it.type == stat.type }

        MatchStatistic(
            type = stat.type,
            homeValue = stat.value,
            awayValue = otherTeamStat?.value,
            homePercentage = parsePercentage(stat.value ?: ""),
            awayPercentage = parsePercentage(otherTeamStat?.value ?: "")
        )
    }
}

// Match DTO to Domain mapping
fun MatchDto.toDomain(): Match {
    return Match(
        id = fixture.id,
        homeTeam = Team(
            id = teams.home.id,
            name = teams.home.name,
            logo = teams.home.logo
        ),
        awayTeam = Team(
            id = teams.away.id,
            name = teams.away.name,
            logo = teams.away.logo
        ),
        league = League(
            id = league.id,
            name = league.name,
            country = league.country,
            logo = league.logo,
            flag = league.flag,
            season = league.season,
            round = league.round
        ),
        fixture = Fixture(
            dateTime = try {
                // Handle different date formats from API
                val cleanDate = fixture.date.replace("Z", "").substringBefore(".")
                LocalDateTime.parse(
                    cleanDate,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                )
            } catch (e: Exception) {
                // Fallback to current time if parsing fails
                LocalDateTime.now()
            },
            timezone = fixture.timezone,
            timestamp = fixture.timestamp
        ),
        score = Score(
            home = goals.home,
            away = goals.away,
            halftime = ScorePeriod(
                home = score.halftime.home,
                away = score.halftime.away
            ),
            fulltime = ScorePeriod(
                home = score.fulltime.home,
                away = score.fulltime.away
            ),
            extratime = score.extratime?.let {
                ScorePeriod(
                    home = it.home,
                    away = it.away
                )
            },
            penalties = score.penalty?.let {
                ScorePeriod(
                    home = it.home,
                    away = it.away
                )
            }
        ),
        status = MatchStatus(
            short = fixture.status.short,
            long = fixture.status.long,
            elapsed = fixture.status.elapsed,
            isLive = fixture.status.short == "1H" || fixture.status.short == "2H" || fixture.status.short == "HT",
            isFinished = fixture.status.short == "FT" || fixture.status.short == "AET" || fixture.status.short == "PEN"
        ),
        venue = fixture.venue?.let { venue ->
            Venue(
                id = venue.id ?: 0,
                name = venue.name ?: "Unknown Venue",
                city = venue.city
            )
        },
        referee = fixture.referee,
        events = events?.map { eventDto -> eventDto.toDomain() } ?: emptyList(),
        lineups = lineups?.map { lineupDto -> lineupDto.toDomain() } ?: emptyList(),
        statistics = statistics?.flatMap { it.toDomain(null) } ?: emptyList()
    )
}

// API DTO mappers
fun ApiEventDto.toDomain(): MatchEvent {
    return MatchEvent(
        time = EventTime(
            elapsed = time.elapsed,
            extra = time.extra
        ),
        team = Team(
            id = team.id,
            name = team.name
        ),
        player = Player(
            id = player.id,
            name = player.name.takeIf { !it.isNullOrBlank() } ?: "Unknown Player"
        ),
        assist = assist?.let {
            Player(
                id = it.id,
                name = it.name.takeIf { !it.isNullOrBlank() } ?: "Unknown Player"
            )
        },
        type = EventType.fromString(type),
        detail = detail ?: "",
        comments = comments
    )
}

fun ApiEventDto.toEntity(matchId: Int): MatchEventEntity {
    return MatchEventEntity(
        matchId = matchId,
        timeElapsed = time.elapsed,
        timeExtra = time.extra,
        teamId = team.id,
        teamName = team.name,
        playerId = player.id,
        playerName = player.name,
        assistId = assist?.id,
        assistName = assist?.name,
        type = type,
        detail = detail,
        comments = comments
    )
}

fun ApiLineupDto.toDomain(): Lineup {
    return Lineup(
        team = Team(
            id = team.id,
            name = team.name
        ),
        formation = formation,
        coach = Coach(
            id = coach.id,
            name = coach.name,
            photo = coach.photo
        ),
        startingEleven = startXI.map { playerPos ->
            LineupPlayer(
                player = Player(
                    id = playerPos.player.id,
                    name = playerPos.player.name,
                    number = playerPos.player.number,
                    position = playerPos.player.pos
                ),
                position = playerPos.player.pos,
                grid = playerPos.player.grid
            )
        },
        substitutes = substitutes.map { playerPos ->
            LineupPlayer(
                player = Player(
                    id = playerPos.player.id,
                    name = playerPos.player.name,
                    number = playerPos.player.number,
                    position = playerPos.player.pos
                ),
                position = playerPos.player.pos,
                grid = playerPos.player.grid
            )
        }
    )
}

fun ApiLineupDto.toEntities(matchId: Int): List<MatchLineupEntity> {
    val entities = mutableListOf<MatchLineupEntity>()

    // Starting XI
    startXI.forEach { playerPos ->
        entities.add(
            MatchLineupEntity(
                matchId = matchId,
                teamId = team.id,
                teamName = team.name,
                formation = formation,
                coachId = coach.id,
                coachName = coach.name,
                coachPhoto = coach.photo,
                playerId = playerPos.player.id,
                playerName = playerPos.player.name,
                playerNumber = playerPos.player.number,
                playerPosition = playerPos.player.pos,
                playerGrid = playerPos.player.grid,
                isStarting = true
            )
        )
    }

    // Substitutes
    substitutes.forEach { playerPos ->
        entities.add(
            MatchLineupEntity(
                matchId = matchId,
                teamId = team.id,
                teamName = team.name,
                formation = formation,
                coachId = coach.id,
                coachName = coach.name,
                coachPhoto = coach.photo,
                playerId = playerPos.player.id,
                playerName = playerPos.player.name,
                playerNumber = playerPos.player.number,
                playerPosition = playerPos.player.pos,
                playerGrid = playerPos.player.grid,
                isStarting = false
            )
        )
    }

    return entities
}

fun ApiStatisticsDto.toDomain(otherTeamStats: ApiStatisticsDto?): List<MatchStatistic> {
    return statistics.map { stat ->
        val otherTeamStat = otherTeamStats?.statistics?.find { it.type == stat.type }

        MatchStatistic(
            type = stat.type,
            homeValue = stat.value,
            awayValue = otherTeamStat?.value,
            homePercentage = parsePercentage(stat.value ?: ""),
            awayPercentage = parsePercentage(otherTeamStat?.value ?: "")
        )
    }
}

fun ApiStatisticsDto.toEntities(matchId: Int): List<MatchStatisticsEntity> {
    return statistics.map { stat ->
        MatchStatisticsEntity(
            matchId = matchId,
            teamId = team.id,
            teamName = team.name,
            type = stat.type,
            value = stat.value
        )
    }
}
