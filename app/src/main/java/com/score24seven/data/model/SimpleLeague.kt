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

// Fixtures models
data class SimpleFixturesResponse(
    @SerializedName("response")
    val response: List<SimpleFixture>?,
    @SerializedName("results")
    val results: Int,
    @SerializedName("errors")
    val errors: List<Any>?
)

data class SimpleFixture(
    @SerializedName("fixture")
    val fixture: SimpleFixtureInfo?,
    @SerializedName("league")
    val league: SimpleFixtureLeague?,
    @SerializedName("teams")
    val teams: SimpleFixtureTeams?,
    @SerializedName("goals")
    val goals: SimpleFixtureGoals?,
    @SerializedName("score")
    val score: SimpleFixtureScore?
)

data class SimpleFixtureInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    val date: String?,
    @SerializedName("status")
    val status: SimpleFixtureStatus?,
    @SerializedName("round")
    val round: String?
)

data class SimpleFixtureStatus(
    @SerializedName("long")
    val long: String?,
    @SerializedName("short")
    val short: String?
)

data class SimpleFixtureLeague(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("round")
    val round: String?
)

data class SimpleFixtureTeams(
    @SerializedName("home")
    val home: SimpleTeam?,
    @SerializedName("away")
    val away: SimpleTeam?
)

data class SimpleFixtureGoals(
    @SerializedName("home")
    val home: Int?,
    @SerializedName("away")
    val away: Int?
)

data class SimpleFixtureScore(
    @SerializedName("halftime")
    val halftime: SimpleFixtureGoals?,
    @SerializedName("fulltime")
    val fulltime: SimpleFixtureGoals?
)

// Top Scorers models
data class SimpleTopScorersResponse(
    @SerializedName("response")
    val response: List<SimpleTopScorer>?,
    @SerializedName("results")
    val results: Int,
    @SerializedName("errors")
    val errors: List<Any>?
)

data class SimpleTopScorer(
    @SerializedName("player")
    val player: SimplePlayer?,
    @SerializedName("statistics")
    val statistics: List<SimplePlayerStats>?
)

data class SimplePlayer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("firstname")
    val firstname: String?,
    @SerializedName("lastname")
    val lastname: String?,
    @SerializedName("age")
    val age: Int?,
    @SerializedName("birth")
    val birth: SimplePlayerBirth?,
    @SerializedName("nationality")
    val nationality: String?,
    @SerializedName("height")
    val height: String?,
    @SerializedName("weight")
    val weight: String?,
    @SerializedName("injured")
    val injured: Boolean?,
    @SerializedName("photo")
    val photo: String?
)

data class SimplePlayerBirth(
    @SerializedName("date")
    val date: String?,
    @SerializedName("place")
    val place: String?,
    @SerializedName("country")
    val country: String?
)

data class SimplePlayerStats(
    @SerializedName("team")
    val team: SimpleTeam?,
    @SerializedName("league")
    val league: SimpleLeague?,
    @SerializedName("games")
    val games: SimplePlayerGames?,
    @SerializedName("substitutes")
    val substitutes: SimplePlayerSubstitutes?,
    @SerializedName("shots")
    val shots: SimplePlayerShots?,
    @SerializedName("goals")
    val goals: SimplePlayerGoals?,
    @SerializedName("passes")
    val passes: SimplePlayerPasses?,
    @SerializedName("tackles")
    val tackles: SimplePlayerTackles?,
    @SerializedName("duels")
    val duels: SimplePlayerDuels?,
    @SerializedName("dribbles")
    val dribbles: SimplePlayerDribbles?,
    @SerializedName("fouls")
    val fouls: SimplePlayerFouls?,
    @SerializedName("cards")
    val cards: SimplePlayerCards?,
    @SerializedName("penalty")
    val penalty: SimplePlayerPenalty?
)

data class SimplePlayerGames(
    @SerializedName("appearences")
    val appearences: Int?,
    @SerializedName("lineups")
    val lineups: Int?,
    @SerializedName("minutes")
    val minutes: Int?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("position")
    val position: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("captain")
    val captain: Boolean?
)

data class SimplePlayerGoals(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("conceded")
    val conceded: Int?,
    @SerializedName("assists")
    val assists: Int?,
    @SerializedName("saves")
    val saves: Int?
)

data class SimplePlayerSubstitutes(
    @SerializedName("in")
    val `in`: Int?,
    @SerializedName("out")
    val out: Int?,
    @SerializedName("bench")
    val bench: Int?
)

data class SimplePlayerShots(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("on")
    val on: Int?
)

data class SimplePlayerPasses(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("key")
    val key: Int?,
    @SerializedName("accuracy")
    val accuracy: Int?
)

data class SimplePlayerTackles(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("blocks")
    val blocks: Int?,
    @SerializedName("interceptions")
    val interceptions: Int?
)

data class SimplePlayerDuels(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("won")
    val won: Int?
)

data class SimplePlayerDribbles(
    @SerializedName("attempts")
    val attempts: Int?,
    @SerializedName("success")
    val success: Int?,
    @SerializedName("past")
    val past: Int?
)

data class SimplePlayerFouls(
    @SerializedName("drawn")
    val drawn: Int?,
    @SerializedName("committed")
    val committed: Int?
)

data class SimplePlayerCards(
    @SerializedName("yellow")
    val yellow: Int?,
    @SerializedName("yellowred")
    val yellowred: Int?,
    @SerializedName("red")
    val red: Int?
)

data class SimplePlayerPenalty(
    @SerializedName("won")
    val won: Int?,
    @SerializedName("commited")
    val commited: Int?,
    @SerializedName("scored")
    val scored: Int?,
    @SerializedName("missed")
    val missed: Int?,
    @SerializedName("saved")
    val saved: Int?
)