/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kickscore.live.data.database.converters.DateTimeConverters
import com.kickscore.live.data.database.converters.ListConverters
import java.time.LocalDateTime

@Entity(tableName = "matches")
@TypeConverters(DateTimeConverters::class, ListConverters::class)
data class MatchEntity(
    @PrimaryKey
    val id: Int,

    // Basic match info
    val homeTeamId: Int,
    val homeTeamName: String,
    val homeTeamLogo: String?,
    val awayTeamId: Int,
    val awayTeamName: String,
    val awayTeamLogo: String?,

    // League info
    val leagueId: Int,
    val leagueName: String,
    val leagueLogo: String?,
    val leagueCountry: String,
    val season: Int?,
    val round: String?,

    // Match timing
    val matchDateTime: LocalDateTime,
    val timezone: String,
    val timestamp: Long,

    // Status
    val status: String,
    val statusLong: String,
    val elapsed: Int?,

    // Scores
    val homeScore: Int?,
    val awayScore: Int?,
    val halftimeHome: Int?,
    val halftimeAway: Int?,
    val fulltimeHome: Int?,
    val fulltimeAway: Int?,

    // Venue
    val venueId: Int?,
    val venueName: String?,
    val venueCity: String?,

    // Additional info
    val referee: String?,
    val winner: String?, // "home", "away", "draw", null

    // Metadata
    val isLive: Boolean = false,
    val isFavorite: Boolean = false,
    val lastUpdated: LocalDateTime = LocalDateTime.now(),

    // Cached data flags
    val hasEvents: Boolean = false,
    val hasLineups: Boolean = false,
    val hasStatistics: Boolean = false
)

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val code: String?,
    val country: String?,
    val founded: Int?,
    val national: Boolean,
    val logo: String?,
    val isFavorite: Boolean = false,
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "leagues")
data class LeagueEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String?, // "League", "Cup"
    val country: String,
    val countryCode: String?,
    val logo: String?,
    val flag: String?,
    val season: Int?,
    val current: Boolean = false,
    val isFavorite: Boolean = false,
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val firstname: String?,
    val lastname: String?,
    val age: Int?,
    val birthDate: String?,
    val birthPlace: String?,
    val birthCountry: String?,
    val nationality: String?,
    val height: String?,
    val weight: String?,
    val injured: Boolean?,
    val photo: String?,
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

@Entity(
    tableName = "match_events",
    primaryKeys = ["matchId", "timeElapsed", "teamId", "playerId", "type"]
)
@TypeConverters(DateTimeConverters::class)
data class MatchEventEntity(
    val matchId: Int,
    val timeElapsed: Int,
    val timeExtra: Int?,
    val teamId: Int,
    val teamName: String,
    val playerId: Int,
    val playerName: String,
    val assistId: Int?,
    val assistName: String?,
    val type: String, // "Goal", "Card", "subst"
    val detail: String, // "Normal Goal", "Yellow Card", etc.
    val comments: String?,
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

@Entity(
    tableName = "match_lineups",
    primaryKeys = ["matchId", "teamId", "playerId"]
)
@TypeConverters(DateTimeConverters::class)
data class MatchLineupEntity(
    val matchId: Int,
    val teamId: Int,
    val teamName: String,
    val formation: String,
    val coachId: Int,
    val coachName: String,
    val coachPhoto: String?,
    val playerId: Int,
    val playerName: String,
    val playerNumber: Int,
    val playerPosition: String,
    val playerGrid: String?,
    val isStarting: Boolean, // true for startXI, false for substitutes
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

@Entity(
    tableName = "match_statistics",
    primaryKeys = ["matchId", "teamId", "type"]
)
@TypeConverters(DateTimeConverters::class)
data class MatchStatisticsEntity(
    val matchId: Int,
    val teamId: Int,
    val teamName: String,
    val type: String, // "Shots on Goal", "Possession", etc.
    val value: String?,
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

@Entity(
    tableName = "standings",
    primaryKeys = ["leagueId", "season", "teamId"]
)
@TypeConverters(DateTimeConverters::class)
data class StandingEntity(
    val leagueId: Int,
    val season: Int,
    val teamId: Int,
    val teamName: String,
    val teamLogo: String?,
    val rank: Int,
    val points: Int,
    val goalsDiff: Int,
    @ColumnInfo(name = "group_name")
    val group: String?,
    val form: String?,
    val status: String?,
    val description: String?,

    // All games stats
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,

    // Home stats
    val homePlayed: Int,
    val homeWins: Int,
    val homeDraws: Int,
    val homeLosses: Int,
    val homeGoalsFor: Int,
    val homeGoalsAgainst: Int,

    // Away stats
    val awayPlayed: Int,
    val awayWins: Int,
    val awayDraws: Int,
    val awayLosses: Int,
    val awayGoalsFor: Int,
    val awayGoalsAgainst: Int,

    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "type", "entityId"]
)
@TypeConverters(DateTimeConverters::class)
data class FavoriteEntity(
    val userId: String = "default", // For multi-user support in future
    val type: String, // "team", "league", "match"
    val entityId: Int,
    val entityName: String,
    val entityLogo: String?,
    val addedAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val id: String, // Unique identifier for the page
    val prevKey: Int?,
    val nextKey: Int?,
    val lastRefresh: Long = System.currentTimeMillis()
)