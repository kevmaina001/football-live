/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kickscore.live.data.database.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams WHERE id = :teamId")
    suspend fun getTeamById(teamId: Int): TeamEntity?

    @Query("SELECT * FROM teams WHERE id = :teamId")
    fun getTeamByIdFlow(teamId: Int): Flow<TeamEntity?>

    @Query("SELECT * FROM teams ORDER BY name ASC")
    fun getAllTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteTeams(): Flow<List<TeamEntity>>

    @Query("""
        SELECT * FROM teams
        WHERE name LIKE '%' || :query || '%'
           OR code LIKE '%' || :query || '%'
           OR country LIKE '%' || :query || '%'
        ORDER BY name ASC
        LIMIT :limit
    """)
    suspend fun searchTeams(query: String, limit: Int = 50): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE country = :country ORDER BY name ASC")
    suspend fun getTeamsByCountry(country: String): List<TeamEntity>

    @Query("UPDATE teams SET isFavorite = :isFavorite WHERE id = :teamId")
    suspend fun setTeamFavorite(teamId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Update
    suspend fun updateTeam(team: TeamEntity)

    @Delete
    suspend fun deleteTeam(team: TeamEntity)

    @Query("DELETE FROM teams WHERE id = :teamId")
    suspend fun deleteTeamById(teamId: Int)

    @Query("SELECT COUNT(*) FROM teams")
    suspend fun getTeamCount(): Int
}