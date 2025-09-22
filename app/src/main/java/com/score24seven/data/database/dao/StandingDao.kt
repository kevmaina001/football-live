/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.score24seven.data.database.entity.StandingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingDao {

    @Query("""
        SELECT * FROM standings
        WHERE leagueId = :leagueId AND season = :season
        ORDER BY rank ASC
    """)
    suspend fun getLeagueStandings(leagueId: Int, season: Int): List<StandingEntity>

    @Query("""
        SELECT * FROM standings
        WHERE leagueId = :leagueId AND season = :season
        ORDER BY rank ASC
    """)
    fun getLeagueStandingsFlow(leagueId: Int, season: Int): Flow<List<StandingEntity>>

    @Query("""
        SELECT * FROM standings
        WHERE leagueId = :leagueId AND season = :season AND group_name = :group
        ORDER BY rank ASC
    """)
    suspend fun getGroupStandings(
        leagueId: Int,
        season: Int,
        group: String
    ): List<StandingEntity>

    @Query("""
        SELECT DISTINCT group_name FROM standings
        WHERE leagueId = :leagueId AND season = :season AND group_name IS NOT NULL
        ORDER BY group_name ASC
    """)
    suspend fun getLeagueGroups(leagueId: Int, season: Int): List<String>

    @Query("""
        SELECT * FROM standings
        WHERE teamId = :teamId AND season = :season
        LIMIT 1
    """)
    suspend fun getTeamStanding(teamId: Int, season: Int): StandingEntity?

    @Query("""
        SELECT * FROM standings
        WHERE teamId = :teamId AND season = :season
        LIMIT 1
    """)
    fun getTeamStandingFlow(teamId: Int, season: Int): Flow<StandingEntity?>

    @Query("""
        SELECT * FROM standings
        WHERE leagueId = :leagueId AND season = :season
        AND rank BETWEEN :startRank AND :endRank
        ORDER BY rank ASC
    """)
    suspend fun getStandingsByRankRange(
        leagueId: Int,
        season: Int,
        startRank: Int,
        endRank: Int
    ): List<StandingEntity>

    // Get top teams
    @Query("""
        SELECT * FROM standings
        WHERE leagueId = :leagueId AND season = :season
        ORDER BY rank ASC
        LIMIT :limit
    """)
    suspend fun getTopTeams(leagueId: Int, season: Int, limit: Int = 5): List<StandingEntity>

    // Get bottom teams
    @Query("""
        SELECT * FROM standings
        WHERE leagueId = :leagueId AND season = :season
        ORDER BY rank DESC
        LIMIT :limit
    """)
    suspend fun getBottomTeams(leagueId: Int, season: Int, limit: Int = 5): List<StandingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStanding(standing: StandingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandings(standings: List<StandingEntity>)

    @Update
    suspend fun updateStanding(standing: StandingEntity)

    @Delete
    suspend fun deleteStanding(standing: StandingEntity)

    @Query("DELETE FROM standings WHERE leagueId = :leagueId AND season = :season")
    suspend fun deleteLeagueStandings(leagueId: Int, season: Int)

    @Transaction
    suspend fun refreshLeagueStandings(leagueId: Int, season: Int, standings: List<StandingEntity>) {
        deleteLeagueStandings(leagueId, season)
        insertStandings(standings)
    }

    @Query("SELECT COUNT(*) FROM standings WHERE leagueId = :leagueId AND season = :season")
    suspend fun getStandingCount(leagueId: Int, season: Int): Int
}