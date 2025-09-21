/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.repository

import com.kickscore.live.data.database.dao.StandingDao
import com.kickscore.live.data.mapper.StandingMapper
import com.kickscore.live.domain.model.Standing
import com.kickscore.live.domain.repository.StandingRepository
import com.kickscore.live.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandingRepositoryImpl @Inject constructor(
    private val standingDao: StandingDao
) : StandingRepository {

    override fun getLeagueStandings(leagueId: Int, season: Int): Flow<Resource<List<Standing>>> = flow {
        try {
            emit(Resource.Loading())

            standingDao.getLeagueStandingsFlow(leagueId, season)
                .map { entities ->
                    StandingMapper.mapEntitiesToDomain(entities)
                }
                .collect { standings ->
                    emit(Resource.Success(standings))
                }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load standings"))
        }
    }

    override suspend fun getLeagueStandingsSync(leagueId: Int, season: Int): Resource<List<Standing>> {
        return try {
            val entities = standingDao.getLeagueStandings(leagueId, season)
            val standings = StandingMapper.mapEntitiesToDomain(entities)
            Resource.Success(standings)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load standings")
        }
    }

    override fun getGroupStandings(leagueId: Int, season: Int, group: String): Flow<Resource<List<Standing>>> = flow {
        try {
            emit(Resource.Loading())

            val entities = standingDao.getGroupStandings(leagueId, season, group)
            val standings = StandingMapper.mapEntitiesToDomain(entities)
            emit(Resource.Success(standings))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load group standings"))
        }
    }

    override suspend fun getTeamStanding(teamId: Int, season: Int): Resource<Standing?> {
        return try {
            val entity = standingDao.getTeamStanding(teamId, season)
            val standing = entity?.let { StandingMapper.mapEntityToDomain(it) }
            Resource.Success(standing)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load team standing")
        }
    }

    override suspend fun getTopTeams(leagueId: Int, season: Int, limit: Int): Resource<List<Standing>> {
        return try {
            val entities = standingDao.getTopTeams(leagueId, season, limit)
            val standings = StandingMapper.mapEntitiesToDomain(entities)
            Resource.Success(standings)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load top teams")
        }
    }

    override suspend fun getBottomTeams(leagueId: Int, season: Int, limit: Int): Resource<List<Standing>> {
        return try {
            val entities = standingDao.getBottomTeams(leagueId, season, limit)
            val standings = StandingMapper.mapEntitiesToDomain(entities)
            Resource.Success(standings)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load bottom teams")
        }
    }

    override suspend fun refreshStandings(leagueId: Int, season: Int): Resource<List<Standing>> {
        return try {
            // For now, just return cached data. In the future, this would fetch from API
            val entities = standingDao.getLeagueStandings(leagueId, season)
            val standings = StandingMapper.mapEntitiesToDomain(entities)
            Resource.Success(standings)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to refresh standings")
        }
    }
}