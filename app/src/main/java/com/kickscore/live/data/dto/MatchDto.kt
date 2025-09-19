/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.dto

import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("fixture")
    val fixture: FixtureDto,

    @SerializedName("league")
    val league: LeagueDto,

    @SerializedName("teams")
    val teams: TeamsDto,

    @SerializedName("goals")
    val goals: GoalsDto,

    @SerializedName("score")
    val score: ScoreDto,

    @SerializedName("events")
    val events: List<EventDto>? = null,

    @SerializedName("lineups")
    val lineups: List<LineupDto>? = null,

    @SerializedName("statistics")
    val statistics: List<StatisticsDto>? = null
)

data class FixtureDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("referee")
    val referee: String?,

    @SerializedName("timezone")
    val timezone: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("periods")
    val periods: PeriodsDto?,

    @SerializedName("venue")
    val venue: VenueDto?,

    @SerializedName("status")
    val status: StatusDto
)

data class StatusDto(
    @SerializedName("long")
    val long: String,

    @SerializedName("short")
    val short: String,

    @SerializedName("elapsed")
    val elapsed: Int?
)

data class PeriodsDto(
    @SerializedName("first")
    val first: Long?,

    @SerializedName("second")
    val second: Long?
)

data class VenueDto(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("city")
    val city: String?
)

data class LeagueDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("logo")
    val logo: String?,

    @SerializedName("flag")
    val flag: String?,

    @SerializedName("season")
    val season: Int?,

    @SerializedName("round")
    val round: String?
)

data class TeamsDto(
    @SerializedName("home")
    val home: TeamDto,

    @SerializedName("away")
    val away: TeamDto
)

data class TeamDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("logo")
    val logo: String?,

    @SerializedName("winner")
    val winner: Boolean?
)

data class GoalsDto(
    @SerializedName("home")
    val home: Int?,

    @SerializedName("away")
    val away: Int?
)

data class ScoreDto(
    @SerializedName("halftime")
    val halftime: GoalsDto,

    @SerializedName("fulltime")
    val fulltime: GoalsDto,

    @SerializedName("extratime")
    val extratime: GoalsDto?,

    @SerializedName("penalty")
    val penalty: GoalsDto?
)

data class EventDto(
    @SerializedName("time")
    val time: TimeDto,

    @SerializedName("team")
    val team: TeamDto,

    @SerializedName("player")
    val player: PlayerDto,

    @SerializedName("assist")
    val assist: PlayerDto?,

    @SerializedName("type")
    val type: String,

    @SerializedName("detail")
    val detail: String,

    @SerializedName("comments")
    val comments: String?
)

data class TimeDto(
    @SerializedName("elapsed")
    val elapsed: Int,

    @SerializedName("extra")
    val extra: Int?
)

data class PlayerDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

data class LineupDto(
    @SerializedName("team")
    val team: TeamDto,

    @SerializedName("coach")
    val coach: CoachDto,

    @SerializedName("formation")
    val formation: String,

    @SerializedName("startXI")
    val startXI: List<PlayerPositionDto>,

    @SerializedName("substitutes")
    val substitutes: List<PlayerPositionDto>
)

data class CoachDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("photo")
    val photo: String?
)

data class PlayerPositionDto(
    @SerializedName("player")
    val player: PlayerDetailDto
)

data class PlayerDetailDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("number")
    val number: Int,

    @SerializedName("pos")
    val position: String,

    @SerializedName("grid")
    val grid: String?
)

data class StatisticsDto(
    @SerializedName("team")
    val team: TeamDto,

    @SerializedName("statistics")
    val statistics: List<StatisticDto>
)

data class StatisticDto(
    @SerializedName("type")
    val type: String,

    @SerializedName("value")
    val value: String?
)