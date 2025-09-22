/*
 * Simple league models following working implementation pattern
 */

package com.score24seven.data.model

import com.google.gson.annotations.SerializedName

data class SimpleLeagueResponse(
    @SerializedName("response")
    val response: List<SimpleLeagueItem>?,
    @SerializedName("results")
    val results: Int,
    @SerializedName("errors")
    val errors: List<Any>?
)

data class SimpleLeagueItem(
    @SerializedName("league")
    val league: SimpleLeague?,
    @SerializedName("country")
    val country: SimpleCountry?,
    @SerializedName("seasons")
    val seasons: List<SimpleSeason>?
)

data class SimpleLeague(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("type")
    val type: String?
)

data class SimpleCountry(
    @SerializedName("name")
    val name: String?,
    @SerializedName("flag")
    val flag: String?,
    @SerializedName("code")
    val code: String?
)

data class SimpleSeason(
    @SerializedName("year")
    val year: Int,
    @SerializedName("current")
    val current: Boolean,
    @SerializedName("coverage")
    val coverage: SimpleCoverage?
)

data class SimpleCoverage(
    @SerializedName("standings")
    val standings: Boolean
)

data class SimpleStandingsResponse(
    @SerializedName("response")
    val response: List<SimpleStandingResponse>?,
    @SerializedName("results")
    val results: Int,
    @SerializedName("errors")
    val errors: List<Any>?
)

data class SimpleStandingResponse(
    @SerializedName("league")
    val league: SimpleStandingLeague?
)

data class SimpleStandingLeague(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("flag")
    val flag: String?,
    @SerializedName("season")
    val season: Int,
    @SerializedName("standings")
    val standings: List<List<SimpleStanding>>?
)

data class SimpleStanding(
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("team")
    val team: SimpleTeam?,
    @SerializedName("points")
    val points: Int,
    @SerializedName("goalsDiff")
    val goalsDiff: Int,
    @SerializedName("form")
    val form: String?,
    @SerializedName("all")
    val all: SimpleStats?,
    @SerializedName("description")
    val description: String?
)

data class SimpleTeam(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("logo")
    val logo: String?
)

data class SimpleStats(
    @SerializedName("played")
    val played: Int,
    @SerializedName("win")
    val win: Int,
    @SerializedName("draw")
    val draw: Int,
    @SerializedName("lose")
    val lose: Int,
    @SerializedName("goals")
    val goals: SimpleGoals?
)

data class SimpleGoals(
    @SerializedName("against")
    val against: Int
    // Note: goalsFor = against + goalsDiff (from parent standing)
)