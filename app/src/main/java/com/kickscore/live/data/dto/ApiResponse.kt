/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("get")
    val get: String,

    @SerializedName("parameters")
    val parameters: Map<String, String>? = null,

    @SerializedName("errors")
    val errors: List<String>? = null,

    @SerializedName("results")
    val results: Int,

    @SerializedName("paging")
    val paging: PagingDto? = null,

    @SerializedName("response")
    val response: T
)

data class PagingDto(
    @SerializedName("current")
    val current: Int,

    @SerializedName("total")
    val total: Int
)

// Specific response types
data class MatchResponse(
    val matches: List<MatchDto>
)

data class LeagueResponse(
    val leagues: List<LeagueInfoDto>
)

data class StandingResponse(
    val standings: List<StandingDto>
)

data class LeagueInfoDto(
    @SerializedName("league")
    val league: LeagueDto,

    @SerializedName("country")
    val country: CountryDto,

    @SerializedName("seasons")
    val seasons: List<SeasonDto>
)

data class CountryDto(
    @SerializedName("name")
    val name: String,

    @SerializedName("code")
    val code: String?,

    @SerializedName("flag")
    val flag: String?
)

data class SeasonDto(
    @SerializedName("year")
    val year: Int,

    @SerializedName("start")
    val start: String,

    @SerializedName("end")
    val end: String,

    @SerializedName("current")
    val current: Boolean,

    @SerializedName("coverage")
    val coverage: CoverageDto?
)

data class CoverageDto(
    @SerializedName("fixtures")
    val fixtures: FixturesCoverageDto?,

    @SerializedName("standings")
    val standings: Boolean?,

    @SerializedName("players")
    val players: Boolean?,

    @SerializedName("top_scorers")
    val topScorers: Boolean?,

    @SerializedName("predictions")
    val predictions: Boolean?
)

data class FixturesCoverageDto(
    @SerializedName("events")
    val events: Boolean?,

    @SerializedName("lineups")
    val lineups: Boolean?,

    @SerializedName("statistics_fixtures")
    val statisticsFixtures: Boolean?,

    @SerializedName("statistics_players")
    val statisticsPlayers: Boolean?
)

data class StandingDto(
    @SerializedName("rank")
    val rank: Int,

    @SerializedName("team")
    val team: TeamDto,

    @SerializedName("points")
    val points: Int,

    @SerializedName("goalsDiff")
    val goalsDiff: Int,

    @SerializedName("group")
    val group: String?,

    @SerializedName("form")
    val form: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("all")
    val all: StandingStatsDto,

    @SerializedName("home")
    val home: StandingStatsDto,

    @SerializedName("away")
    val away: StandingStatsDto,

    @SerializedName("update")
    val update: String
)

data class StandingStatsDto(
    @SerializedName("played")
    val played: Int,

    @SerializedName("win")
    val win: Int,

    @SerializedName("draw")
    val draw: Int,

    @SerializedName("lose")
    val lose: Int,

    @SerializedName("goals")
    val goals: StandingGoalsDto
)

data class StandingGoalsDto(
    @SerializedName("for")
    val goalsFor: Int,

    @SerializedName("against")
    val goalsAgainst: Int
)