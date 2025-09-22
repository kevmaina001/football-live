/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.repository

import com.score24seven.data.database.dao.TeamDao
import com.score24seven.data.mapper.TeamMapper
import com.score24seven.domain.model.Team
import com.score24seven.domain.repository.TeamRepository
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepositoryImpl @Inject constructor(
    private val teamDao: TeamDao
) : TeamRepository {

    override suspend fun getTeamById(teamId: Int): Resource<Team?> {
        return try {
            val entity = teamDao.getTeamById(teamId)
            val team = entity?.let { TeamMapper.mapEntityToDomain(it) }
            Resource.Success(team)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load team")
        }
    }

    override fun getTeamByIdFlow(teamId: Int): Flow<Resource<Team?>> = flow {
        try {
            emit(Resource.Loading())

            teamDao.getTeamByIdFlow(teamId)
                .map { entity ->
                    entity?.let { TeamMapper.mapEntityToDomain(it) }
                }
                .collect { team ->
                    emit(Resource.Success(team))
                }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load team"))
        }
    }

    override fun getAllTeams(): Flow<Resource<List<Team>>> = flow {
        try {
            emit(Resource.Loading())

            teamDao.getAllTeams()
                .map { entities ->
                    TeamMapper.mapEntitiesToDomain(entities)
                }
                .collect { teams ->
                    emit(Resource.Success(teams))
                }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load teams"))
        }
    }

    override fun getFavoriteTeams(): Flow<Resource<List<Team>>> = flow {
        try {
            emit(Resource.Loading())

            teamDao.getFavoriteTeams()
                .map { entities ->
                    TeamMapper.mapEntitiesToDomain(entities)
                }
                .collect { teams ->
                    emit(Resource.Success(teams))
                }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load favorite teams"))
        }
    }

    override suspend fun getLeagueTeams(leagueId: Int, season: Int): Flow<Resource<List<Team>>> = flow {
        try {
            emit(Resource.Loading())

            // For now, return all teams. In the future, this would filter by league
            teamDao.getAllTeams()
                .map { entities ->
                    TeamMapper.mapEntitiesToDomain(entities)
                }
                .collect { teams ->
                    emit(Resource.Success(teams))
                }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load league teams"))
        }
    }

    override suspend fun searchTeams(query: String, limit: Int): Resource<List<Team>> {
        return try {
            val entities = teamDao.searchTeams(query, limit)
            val teams = TeamMapper.mapEntitiesToDomain(entities)
            Resource.Success(teams)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to search teams")
        }
    }

    override suspend fun getTeamsByCountry(country: String): Resource<List<Team>> {
        return try {
            val entities = teamDao.getTeamsByCountry(country)
            val teams = TeamMapper.mapEntitiesToDomain(entities)
            Resource.Success(teams)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load teams by country")
        }
    }

    override suspend fun setTeamFavorite(teamId: Int, isFavorite: Boolean): Resource<Unit> {
        return try {
            teamDao.setTeamFavorite(teamId, isFavorite)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update team favorite status")
        }
    }

    override suspend fun refreshTeams(): Resource<List<Team>> {
        return try {
            // For now, just return cached data. In the future, this would fetch from API
            val entities = teamDao.getAllTeams()
            // Since getAllTeams returns Flow, we need to collect it
            var teams: List<Team> = emptyList()
            entities.collect { entitiesList ->
                teams = TeamMapper.mapEntitiesToDomain(entitiesList)
            }
            Resource.Success(teams)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to refresh teams")
        }
    }
}