/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.repository

import com.score24seven.data.api.FootballApiService
import com.score24seven.data.database.dao.StandingDao
import com.score24seven.data.mapper.StandingMapper
import com.score24seven.domain.model.Standing
import com.score24seven.domain.repository.StandingRepository
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandingRepositoryImpl @Inject constructor(
    private val apiService: FootballApiService,
    private val standingDao: StandingDao
) : StandingRepository {

    override fun getLeagueStandings(leagueId: Int, season: Int): Flow<Resource<List<Standing>>> = flow {
        try {
            emit(Resource.Loading())

            println("🏆 DEBUG: StandingRepository - Fetching standings for league $leagueId, season $season")

            // First, try to get cached data from database
            val cachedStandings = standingDao.getLeagueStandings(leagueId, season)
            if (cachedStandings.isNotEmpty()) {
                println("🏆 DEBUG: StandingRepository - Found ${cachedStandings.size} cached standings")
                val domainStandings = StandingMapper.mapEntitiesToDomain(cachedStandings)
                emit(Resource.Success(domainStandings))
                return@flow
            }

            // If no cached data, fetch from API
            println("🏆 DEBUG: StandingRepository - No cached data, fetching from API")
            val response = apiService.getLeagueStandings(leagueId, season)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                println("🏆 DEBUG: StandingRepository - API Response received")
                println("🏆 DEBUG: StandingRepository - Response is null: ${apiResponse == null}")
                println("🏆 DEBUG: StandingRepository - Response.response is null: ${apiResponse?.response == null}")
                println("🏆 DEBUG: StandingRepository - Response.response size: ${apiResponse?.response?.size ?: 0}")

                if (apiResponse?.response != null && apiResponse.response.isNotEmpty()) {
                    // Map API response to domain models with proper null checks
                    val standings = apiResponse.response.flatMap { standingResponse ->
                        standingResponse.standings.flatMap { standingsGroup ->
                            standingsGroup.mapNotNull { standingDto ->
                                try {
                                    StandingMapper.mapDtoToDomain(standingDto, standingResponse.league)
                                } catch (e: Exception) {
                                    println("🔴 DEBUG: Error mapping standing: ${e.message}")
                                    null
                                }
                            }
                        }
                    }

                    println("🏆 DEBUG: StandingRepository - Mapped ${standings.size} standings")

                    // Cache in database
                    val entities = StandingMapper.mapDomainToEntities(standings, leagueId, season)
                    standingDao.insertStandings(entities)

                    emit(Resource.Success(standings))
                } else {
                    println("🏆 DEBUG: StandingRepository - No standings data in API response")
                    emit(Resource.Success(emptyList()))
                }
            } else {
                val errorMsg = "API Error: ${response.code()} - ${response.message()}"
                println("🔴 DEBUG: StandingRepository - $errorMsg")
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            val errorMsg = "Failed to load standings: ${e.message}"
            println("🔴 DEBUG: StandingRepository - Exception: $errorMsg")
            emit(Resource.Error(errorMsg))
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
            println("🏆 DEBUG: StandingRepository - Refreshing standings for league $leagueId")

            // Delete cached data
            standingDao.deleteLeagueStandings(leagueId, season)

            // Fetch fresh data from API
            val response = apiService.getLeagueStandings(leagueId, season)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.response != null && apiResponse.response.isNotEmpty()) {
                    val standings = apiResponse.response.flatMap { standingResponse ->
                        standingResponse.standings.flatMap { standingsGroup ->
                            standingsGroup.mapNotNull { standingDto ->
                                try {
                                    StandingMapper.mapDtoToDomain(standingDto, standingResponse.league)
                                } catch (e: Exception) {
                                    println("🔴 DEBUG: Error mapping standing in refresh: ${e.message}")
                                    null
                                }
                            }
                        }
                    }

                    // Cache in database
                    val entities = StandingMapper.mapDomainToEntities(standings, leagueId, season)
                    standingDao.insertStandings(entities)

                    Resource.Success(standings)
                } else {
                    Resource.Success(emptyList())
                }
            } else {
                Resource.Error("Failed to refresh standings: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to refresh standings")
        }
    }
}