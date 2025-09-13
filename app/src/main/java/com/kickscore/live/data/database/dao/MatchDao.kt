/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kickscore.live.data.database.entity.FavoriteEntity
import com.kickscore.live.data.database.entity.MatchEntity
import com.kickscore.live.data.database.entity.MatchEventEntity
import com.kickscore.live.data.database.entity.MatchLineupEntity
import com.kickscore.live.data.database.entity.MatchStatisticsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MatchDao {

    // Basic match queries
    @Query("SELECT * FROM matches WHERE id = :matchId")
    suspend fun getMatchById(matchId: Int): MatchEntity?

    @Query("SELECT * FROM matches WHERE id = :matchId")
    fun getMatchByIdFlow(matchId: Int): Flow<MatchEntity?>

    @Query("SELECT * FROM matches ORDER BY matchDateTime ASC")
    fun getAllMatchesPagingSource(): PagingSource<Int, MatchEntity>

    // Live matches
    @Query("SELECT * FROM matches WHERE isLive = 1 ORDER BY matchDateTime ASC")
    fun getLiveMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE isLive = 1 ORDER BY matchDateTime ASC")
    suspend fun getLiveMatchesList(): List<MatchEntity>

    // Today's matches
    @Query("""
        SELECT * FROM matches
        WHERE DATE(matchDateTime) = DATE(:date)
        ORDER BY matchDateTime ASC
    """)
    fun getMatchesByDate(date: LocalDateTime): Flow<List<MatchEntity>>

    @Query("""
        SELECT * FROM matches
        WHERE DATE(matchDateTime) = DATE('now', 'localtime')
        ORDER BY matchDateTime ASC
    """)
    fun getTodayMatches(): Flow<List<MatchEntity>>

    // Date range queries
    @Query("""
        SELECT * FROM matches
        WHERE matchDateTime BETWEEN :startDate AND :endDate
        ORDER BY matchDateTime ASC
    """)
    fun getMatchesByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<MatchEntity>>

    @Query("""
        SELECT * FROM matches
        WHERE matchDateTime BETWEEN :startDate AND :endDate
        ORDER BY matchDateTime ASC
    """)
    fun getMatchesByDateRangePaging(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): PagingSource<Int, MatchEntity>

    // League matches
    @Query("""
        SELECT * FROM matches
        WHERE leagueId = :leagueId AND season = :season
        ORDER BY matchDateTime DESC
    """)
    fun getLeagueMatches(leagueId: Int, season: Int): Flow<List<MatchEntity>>

    @Query("""
        SELECT * FROM matches
        WHERE leagueId = :leagueId AND season = :season
        ORDER BY matchDateTime DESC
    """)
    fun getLeagueMatchesPaging(leagueId: Int, season: Int): PagingSource<Int, MatchEntity>

    // Team matches
    @Query("""
        SELECT * FROM matches
        WHERE (homeTeamId = :teamId OR awayTeamId = :teamId) AND season = :season
        ORDER BY matchDateTime DESC
    """)
    fun getTeamMatches(teamId: Int, season: Int): Flow<List<MatchEntity>>

    @Query("""
        SELECT * FROM matches
        WHERE (homeTeamId = :teamId OR awayTeamId = :teamId) AND season = :season
        ORDER BY matchDateTime DESC
        LIMIT :limit
    """)
    suspend fun getRecentTeamMatches(teamId: Int, season: Int, limit: Int): List<MatchEntity>

    @Query("""
        SELECT * FROM matches
        WHERE (homeTeamId = :teamId OR awayTeamId = :teamId)
        AND matchDateTime > :currentDate AND season = :season
        ORDER BY matchDateTime ASC
        LIMIT :limit
    """)
    suspend fun getUpcomingTeamMatches(
        teamId: Int,
        season: Int,
        currentDate: LocalDateTime,
        limit: Int
    ): List<MatchEntity>

    // Head to head
    @Query("""
        SELECT * FROM matches
        WHERE (homeTeamId = :team1Id AND awayTeamId = :team2Id)
           OR (homeTeamId = :team2Id AND awayTeamId = :team1Id)
        ORDER BY matchDateTime DESC
        LIMIT :limit
    """)
    suspend fun getHeadToHeadMatches(
        team1Id: Int,
        team2Id: Int,
        limit: Int = 10
    ): List<MatchEntity>

    // Favorite matches
    @Query("""
        SELECT m.* FROM matches m
        INNER JOIN favorites f ON f.entityId = m.id
        WHERE f.type = 'match' AND f.userId = :userId
        ORDER BY m.matchDateTime DESC
    """)
    fun getFavoriteMatches(userId: String = "default"): Flow<List<MatchEntity>>

    @Query("""
        SELECT m.* FROM matches m
        WHERE (m.homeTeamId IN (
            SELECT f.entityId FROM favorites f
            WHERE f.type = 'team' AND f.userId = :userId
        ) OR m.awayTeamId IN (
            SELECT f.entityId FROM favorites f
            WHERE f.type = 'team' AND f.userId = :userId
        ))
        ORDER BY m.matchDateTime DESC
    """)
    fun getMatchesFromFavoriteTeams(userId: String = "default"): Flow<List<MatchEntity>>

    // Search
    @Query("""
        SELECT * FROM matches
        WHERE homeTeamName LIKE '%' || :query || '%'
           OR awayTeamName LIKE '%' || :query || '%'
           OR leagueName LIKE '%' || :query || '%'
        ORDER BY matchDateTime DESC
        LIMIT :limit
    """)
    suspend fun searchMatches(query: String, limit: Int = 50): List<MatchEntity>

    // Status updates
    @Query("UPDATE matches SET isLive = :isLive WHERE id = :matchId")
    suspend fun updateMatchLiveStatus(matchId: Int, isLive: Boolean)

    @Query("""
        UPDATE matches SET
        homeScore = :homeScore,
        awayScore = :awayScore,
        status = :status,
        statusLong = :statusLong,
        elapsed = :elapsed,
        isLive = :isLive,
        lastUpdated = :lastUpdated
        WHERE id = :matchId
    """)
    suspend fun updateMatchScore(
        matchId: Int,
        homeScore: Int?,
        awayScore: Int?,
        status: String,
        statusLong: String,
        elapsed: Int?,
        isLive: Boolean,
        lastUpdated: LocalDateTime
    )

    // Insert/Update/Delete operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Delete
    suspend fun deleteMatch(match: MatchEntity)

    @Query("DELETE FROM matches WHERE id = :matchId")
    suspend fun deleteMatchById(matchId: Int)

    @Query("DELETE FROM matches WHERE lastUpdated < :cutoffDate")
    suspend fun deleteOldMatches(cutoffDate: LocalDateTime)

    // Cache management
    @Query("SELECT COUNT(*) FROM matches WHERE isLive = 1")
    suspend fun getLiveMatchCount(): Int

    @Query("SELECT MAX(lastUpdated) FROM matches WHERE isLive = 1")
    suspend fun getLastLiveMatchUpdate(): LocalDateTime?

    @Transaction
    suspend fun refreshLiveMatches(matches: List<MatchEntity>) {
        // Mark all as not live first
        setAllMatchesNotLive()
        // Insert/update new live matches
        insertMatches(matches)
    }

    @Query("UPDATE matches SET isLive = 0")
    suspend fun setAllMatchesNotLive()
}