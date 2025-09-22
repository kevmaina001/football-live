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
import androidx.room.Update
import com.score24seven.data.database.entity.LeagueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LeagueDao {

    @Query("SELECT * FROM leagues WHERE id = :leagueId")
    suspend fun getLeagueById(leagueId: Int): LeagueEntity?

    @Query("SELECT * FROM leagues WHERE id = :leagueId")
    fun getLeagueByIdFlow(leagueId: Int): Flow<LeagueEntity?>

    @Query("SELECT * FROM leagues ORDER BY name ASC")
    fun getAllLeagues(): Flow<List<LeagueEntity>>

    @Query("SELECT * FROM leagues WHERE current = 1 ORDER BY name ASC")
    fun getCurrentLeagues(): Flow<List<LeagueEntity>>

    @Query("SELECT * FROM leagues WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteLeagues(): Flow<List<LeagueEntity>>

    @Query("SELECT * FROM leagues WHERE country = :country ORDER BY name ASC")
    suspend fun getLeaguesByCountry(country: String): List<LeagueEntity>

    @Query("SELECT DISTINCT country FROM leagues ORDER BY country ASC")
    suspend fun getAllCountries(): List<String>

    @Query("""
        SELECT * FROM leagues
        WHERE name LIKE '%' || :query || '%'
           OR country LIKE '%' || :query || '%'
        ORDER BY name ASC
        LIMIT :limit
    """)
    suspend fun searchLeagues(query: String, limit: Int = 50): List<LeagueEntity>

    @Query("UPDATE leagues SET isFavorite = :isFavorite WHERE id = :leagueId")
    suspend fun setLeagueFavorite(leagueId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: LeagueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagues(leagues: List<LeagueEntity>)

    @Update
    suspend fun updateLeague(league: LeagueEntity)

    @Delete
    suspend fun deleteLeague(league: LeagueEntity)

    @Query("DELETE FROM leagues WHERE id = :leagueId")
    suspend fun deleteLeagueById(leagueId: Int)

    @Query("SELECT COUNT(*) FROM leagues")
    suspend fun getLeagueCount(): Int
}