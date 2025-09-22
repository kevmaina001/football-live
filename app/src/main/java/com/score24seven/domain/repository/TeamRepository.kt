/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.repository

import com.score24seven.domain.model.Team
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun getTeamById(teamId: Int): Resource<Team?>

    fun getTeamByIdFlow(teamId: Int): Flow<Resource<Team?>>

    fun getAllTeams(): Flow<Resource<List<Team>>>

    fun getFavoriteTeams(): Flow<Resource<List<Team>>>

    suspend fun getLeagueTeams(leagueId: Int, season: Int): Flow<Resource<List<Team>>>

    suspend fun searchTeams(query: String, limit: Int = 50): Resource<List<Team>>

    suspend fun getTeamsByCountry(country: String): Resource<List<Team>>

    suspend fun setTeamFavorite(teamId: Int, isFavorite: Boolean): Resource<Unit>

    suspend fun refreshTeams(): Resource<List<Team>>
}