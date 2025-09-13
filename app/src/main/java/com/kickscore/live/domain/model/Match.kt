/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.model

import java.time.LocalDateTime

data class Match(
    val id: Int,
    val homeTeam: Team,
    val awayTeam: Team,
    val league: League,
    val fixture: Fixture,
    val score: Score,
    val status: MatchStatus,
    val venue: Venue? = null,
    val referee: String? = null,
    val statistics: List<MatchStatistic> = emptyList(),
    val events: List<MatchEvent> = emptyList(),
    val lineups: List<Lineup> = emptyList(),
    val isFavorite: Boolean = false,
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

data class Team(
    val id: Int,
    val name: String,
    val code: String? = null,
    val logo: String? = null,
    val country: String? = null,
    val founded: Int? = null,
    val isNational: Boolean = false,
    val isFavorite: Boolean = false
)

data class League(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String? = null,
    val flag: String? = null,
    val season: Int? = null,
    val round: String? = null,
    val type: String? = null,
    val isFavorite: Boolean = false
)

data class Fixture(
    val dateTime: LocalDateTime,
    val timezone: String,
    val timestamp: Long
)

data class Score(
    val home: Int?,
    val away: Int?,
    val halftime: ScorePeriod? = null,
    val fulltime: ScorePeriod? = null,
    val extratime: ScorePeriod? = null,
    val penalties: ScorePeriod? = null
)

data class ScorePeriod(
    val home: Int?,
    val away: Int?
)

data class MatchStatus(
    val short: String,
    val long: String,
    val elapsed: Int? = null,
    val isLive: Boolean = false,
    val isFinished: Boolean = false
)

data class Venue(
    val id: Int,
    val name: String,
    val city: String? = null,
    val capacity: Int? = null,
    val surface: String? = null
)

data class MatchStatistic(
    val type: String,
    val homeValue: String?,
    val awayValue: String?,
    val homePercentage: Float? = null,
    val awayPercentage: Float? = null
)

data class MatchEvent(
    val time: EventTime,
    val team: Team,
    val player: Player,
    val assist: Player? = null,
    val type: EventType,
    val detail: String,
    val comments: String? = null
)

data class EventTime(
    val elapsed: Int,
    val extra: Int? = null
)

data class Player(
    val id: Int,
    val name: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val nationality: String? = null,
    val position: String? = null,
    val number: Int? = null,
    val photo: String? = null,
    val isInjured: Boolean = false,
    val statistics: List<PlayerStatistic> = emptyList()
)

data class PlayerStatistic(
    val type: String,
    val value: String,
    val category: String // "offensive", "defensive", "passing", etc.
)

data class Lineup(
    val team: Team,
    val formation: String,
    val coach: Coach,
    val startingEleven: List<LineupPlayer>,
    val substitutes: List<LineupPlayer>
)

data class LineupPlayer(
    val player: Player,
    val position: String,
    val grid: String? = null
)

data class Coach(
    val id: Int,
    val name: String,
    val photo: String? = null,
    val age: Int? = null,
    val nationality: String? = null
)

enum class EventType(val displayName: String, val iconCode: String) {
    GOAL("Goal", "⚽"),
    YELLOW_CARD("Yellow Card", "🟨"),
    RED_CARD("Red Card", "🟥"),
    SUBSTITUTION("Substitution", "🔄"),
    PENALTY("Penalty", "⚽"),
    OWN_GOAL("Own Goal", "⚽"),
    MISSED_PENALTY("Missed Penalty", "❌"),
    VAR("VAR", "📺"),
    OFFSIDE("Offside", "🚩");

    companion object {
        fun fromString(value: String): EventType {
            return when (value.uppercase()) {
                "GOAL", "NORMAL_GOAL" -> GOAL
                "YELLOW_CARD", "CARD" -> YELLOW_CARD
                "RED_CARD" -> RED_CARD
                "SUBSTITUTION", "SUBST" -> SUBSTITUTION
                "PENALTY" -> PENALTY
                "OWN_GOAL" -> OWN_GOAL
                "MISSED_PENALTY" -> MISSED_PENALTY
                "VAR" -> VAR
                "OFFSIDE" -> OFFSIDE
                else -> GOAL
            }
        }
    }
}

// Extension functions for convenience
fun Match.isLive(): Boolean = status.isLive

fun Match.isFinished(): Boolean = status.isFinished

fun Match.hasScore(): Boolean = score.home != null && score.away != null

fun Match.getScoreDisplay(): String {
    return if (hasScore()) {
        "${score.home} - ${score.away}"
    } else {
        "- : -"
    }
}

fun Match.getWinner(): Team? {
    if (!hasScore() || !isFinished()) return null

    return when {
        score.home!! > score.away!! -> homeTeam
        score.away!! > score.home!! -> awayTeam
        else -> null // Draw
    }
}

fun Match.isDraw(): Boolean {
    return hasScore() && isFinished() && score.home == score.away
}

fun Match.getTimeDisplay(): String {
    return when {
        status.isLive && status.elapsed != null -> "${status.elapsed}'"
        status.short == "HT" -> "HT"
        status.short == "FT" -> "FT"
        status.short == "NS" -> fixture.dateTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        else -> status.short
    }
}

fun Team.getInitials(): String {
    return name.split(" ")
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")
        .ifEmpty { name.take(2).uppercase() }
}