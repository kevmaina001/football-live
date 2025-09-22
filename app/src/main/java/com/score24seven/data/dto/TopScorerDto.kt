/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.dto

import com.google.gson.annotations.SerializedName

data class TopScorerDto(
    @SerializedName("player")
    val player: TopScorerPlayerDto,

    @SerializedName("statistics")
    val statistics: List<PlayerStatisticsDto>
)

data class TopScorerPlayerDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("firstname")
    val firstname: String?,

    @SerializedName("lastname")
    val lastname: String?,

    @SerializedName("age")
    val age: Int?,

    @SerializedName("birth")
    val birth: PlayerBirthDto?,

    @SerializedName("nationality")
    val nationality: String?,

    @SerializedName("height")
    val height: String?,

    @SerializedName("weight")
    val weight: String?,

    @SerializedName("injured")
    val injured: Boolean,

    @SerializedName("photo")
    val photo: String?
)

data class PlayerBirthDto(
    @SerializedName("date")
    val date: String?,

    @SerializedName("place")
    val place: String?,

    @SerializedName("country")
    val country: String?
)

data class PlayerStatisticsDto(
    @SerializedName("team")
    val team: TeamDto,

    @SerializedName("league")
    val league: LeagueDto,

    @SerializedName("games")
    val games: PlayerGamesDto,

    @SerializedName("substitutes")
    val substitutes: PlayerSubstitutesDto?,

    @SerializedName("shots")
    val shots: PlayerShotsDto?,

    @SerializedName("goals")
    val goals: PlayerGoalsDto,

    @SerializedName("passes")
    val passes: PlayerPassesDto?,

    @SerializedName("tackles")
    val tackles: PlayerTacklesDto?,

    @SerializedName("duels")
    val duels: PlayerDuelsDto?,

    @SerializedName("dribbles")
    val dribbles: PlayerDribblesDto?,

    @SerializedName("fouls")
    val fouls: PlayerFoulsDto?,

    @SerializedName("cards")
    val cards: PlayerCardsDto?,

    @SerializedName("penalty")
    val penalty: PlayerPenaltyDto?
)

data class PlayerGamesDto(
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
    val captain: Boolean
)

data class PlayerSubstitutesDto(
    @SerializedName("in")
    val substitutesIn: Int?,

    @SerializedName("out")
    val substitutesOut: Int?,

    @SerializedName("bench")
    val bench: Int?
)

data class PlayerShotsDto(
    @SerializedName("total")
    val total: Int?,

    @SerializedName("on")
    val on: Int?
)

data class PlayerGoalsDto(
    @SerializedName("total")
    val total: Int?,

    @SerializedName("conceded")
    val conceded: Int?,

    @SerializedName("assists")
    val assists: Int?,

    @SerializedName("saves")
    val saves: Int?
)

data class PlayerPassesDto(
    @SerializedName("total")
    val total: Int?,

    @SerializedName("key")
    val key: Int?,

    @SerializedName("accuracy")
    val accuracy: Int?
)

data class PlayerTacklesDto(
    @SerializedName("total")
    val total: Int?,

    @SerializedName("blocks")
    val blocks: Int?,

    @SerializedName("interceptions")
    val interceptions: Int?
)

data class PlayerDuelsDto(
    @SerializedName("total")
    val total: Int?,

    @SerializedName("won")
    val won: Int?
)

data class PlayerDribblesDto(
    @SerializedName("attempts")
    val attempts: Int?,

    @SerializedName("success")
    val success: Int?,

    @SerializedName("past")
    val past: Int?
)

data class PlayerFoulsDto(
    @SerializedName("drawn")
    val drawn: Int?,

    @SerializedName("committed")
    val committed: Int?
)

data class PlayerCardsDto(
    @SerializedName("yellow")
    val yellow: Int?,

    @SerializedName("yellowred")
    val yellowred: Int?,

    @SerializedName("red")
    val red: Int?
)

data class PlayerPenaltyDto(
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