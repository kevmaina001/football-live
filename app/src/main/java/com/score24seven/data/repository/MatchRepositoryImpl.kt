/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.score24seven.data.api.FootballApiService
import com.score24seven.data.cache.ApiCacheManager
import com.score24seven.data.database.Score24SevenDatabase
import com.score24seven.data.mapper.MatchMapper
import com.score24seven.data.mapper.MatchEntityMapper
import com.score24seven.data.mock.MockDataProvider
import com.score24seven.data.paging.MatchRemoteMediator
import com.score24seven.data.websocket.LiveMatchService
import com.score24seven.domain.model.Match
import com.score24seven.domain.repository.MatchRepository
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepositoryImpl @Inject constructor(
    private val apiService: FootballApiService,
    private val database: Score24SevenDatabase,
    private val liveMatchService: LiveMatchService,
    private val cacheManager: ApiCacheManager,
    private val mockDataProvider: MockDataProvider
) : MatchRepository {

    private val matchDao = database.matchDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getMatchesPaged(): Flow<PagingData<Match>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = MatchRemoteMediator(
                apiService = apiService,
                database = database
            )
        ) {
            matchDao.getAllMatchesPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { entity ->
                MatchEntityMapper.mapEntityToDomain(entity)
            }
        }
    }

    override fun getLiveMatches(): Flow<Resource<List<Match>>> = flow {
        println("🟡 DEBUG: Starting getLiveMatches() call")
        emit(Resource.Loading())

        try {
            // Always emit cached data first
            val cachedMatches = matchDao.getLiveMatchesList()
            println("🟡 DEBUG: Found ${cachedMatches.size} cached live matches")

            if (cachedMatches.isNotEmpty()) {
                emit(Resource.Success(
                    data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                ))
            }

            // Only fetch from API if cache allows it
            if (cacheManager.shouldFetchLiveMatches()) {
                println("🟡 DEBUG: Cache allows API call - Making API call to getLiveMatches()")
                val response = apiService.getLiveMatches()
                println("🟡 DEBUG: API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        println("🟡 DEBUG: API returned ${apiResponse.response.size} live matches")
                        val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                        matchDao.refreshLiveMatches(entities)
                        cacheManager.markLiveMatchesFetched()

                        emit(Resource.Success(
                            data = entities.map { MatchEntityMapper.mapEntityToDomain(it) }
                        ))
                    } ?: run {
                        println("🔴 DEBUG: API response body was null")
                        emit(Resource.Error(
                            message = "API response body was null",
                            data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                        ))
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        403 -> "API key invalid or expired. Using offline data."
                        429 -> "Rate limit exceeded. Using cached data."
                        else -> "API temporarily unavailable. Using cached data."
                    }
                    println("🔴 DEBUG: API Error ${response.code()}: $errorMsg")

                    // Use mock data if API is completely unavailable (403/429)
                    if (mockDataProvider.shouldUseMockData(response.code()) && cachedMatches.isEmpty()) {
                        println("🟡 DEBUG: Using mock live matches data")
                        mockDataProvider.setUsingMockData(true)
                        emit(Resource.Success(
                            data = mockDataProvider.getMockLiveMatches()
                        ))
                    } else {
                        emit(Resource.Error(
                            message = errorMsg,
                            data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                        ))
                    }
                }
            } else {
                println("🟡 DEBUG: Cache prevents API call - using cached data only")
                // If we have cached data, emit success; otherwise use mock data
                if (cachedMatches.isNotEmpty()) {
                    emit(Resource.Success(
                        data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                } else {
                    println("🟡 DEBUG: No cached data available, using mock data for live matches")
                    mockDataProvider.setUsingMockData(true)
                    emit(Resource.Success(
                        data = mockDataProvider.getMockLiveMatches()
                    ))
                }
            }
        } catch (e: Exception) {
            val errorMsg = "Exception in getLiveMatches: ${e.localizedMessage}"
            println("🔴 DEBUG: $errorMsg")
            e.printStackTrace()

            // Use mock data for network issues or when no cached data available
            val cachedData = matchDao.getLiveMatchesList().map { MatchEntityMapper.mapEntityToDomain(it) }
            if (mockDataProvider.shouldUseMockDataForException(e) || cachedData.isEmpty()) {
                println("🟡 DEBUG: Network issue detected or no cached data, using mock data")
                mockDataProvider.setUsingMockData(true)
                emit(Resource.Success(
                    data = mockDataProvider.getMockLiveMatches()
                ))
            } else {
                emit(Resource.Error(
                    message = errorMsg,
                    data = cachedData
                ))
            }
        }
    }

    override fun getLiveMatchesFlow(): Flow<List<Match>> {
        return combine(
            matchDao.getLiveMatches(),
            liveMatchService.liveUpdates
        ) { cachedMatches, liveUpdate ->
            // Update cached matches with live data
            val updatedMatches = cachedMatches.map { entity ->
                if (entity.id == liveUpdate.matchId) {
                    MatchMapper.updateEntityWithLiveData(entity, liveUpdate)
                } else {
                    entity
                }
            }

            updatedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
        }
    }

    override fun getTodayMatches(): Flow<Resource<List<Match>>> = flow {
        println("🟡 DEBUG: Starting getTodayMatches() call")
        emit(Resource.Loading())

        try {
            // Get today's date
            val today = LocalDateTime.now().toLocalDate().atStartOfDay()
            val todayString = today.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
            println("🟡 DEBUG: Fetching matches for date: $todayString")

            // Always emit cached data first
            val cachedMatches = matchDao.getTodayMatchesList()
            println("🟡 DEBUG: Found ${cachedMatches.size} cached today's matches")

            if (cachedMatches.isNotEmpty()) {
                emit(Resource.Success(
                    data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                ))
            }

            // Only fetch from API if cache allows it
            if (cacheManager.shouldFetchTodayMatches()) {
                println("🟡 DEBUG: Cache allows API call - Making API call to getTodayFixtures($todayString)")
                val response = apiService.getTodayFixtures(todayString)
                println("🟡 DEBUG: API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        println("🟡 DEBUG: API returned ${apiResponse.response.size} today's matches")
                        val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                        matchDao.insertMatches(entities)
                        cacheManager.markTodayMatchesFetched()

                        emit(Resource.Success(
                            data = entities.map { MatchEntityMapper.mapEntityToDomain(it) }
                        ))
                    } ?: run {
                        println("🔴 DEBUG: API response body was null for today's matches")
                        emit(Resource.Error(
                            message = "API response body was null",
                            data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                        ))
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        403 -> "API key invalid or expired. Using offline data."
                        429 -> "Rate limit exceeded. Using cached data."
                        else -> "API temporarily unavailable. Using cached data."
                    }
                    println("🔴 DEBUG: API Error ${response.code()}: $errorMsg")

                    // Use mock data if API is completely unavailable (403/429)
                    if (mockDataProvider.shouldUseMockData(response.code()) && cachedMatches.isEmpty()) {
                        println("🟡 DEBUG: Using mock today's matches data")
                        mockDataProvider.setUsingMockData(true)
                        emit(Resource.Success(
                            data = mockDataProvider.getMockTodayMatches()
                        ))
                    } else {
                        emit(Resource.Error(
                            message = errorMsg,
                            data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                        ))
                    }
                }
            } else {
                println("🟡 DEBUG: Cache prevents API call - using cached data only")
                // If we have cached data, emit success; otherwise emit error asking user to wait
                if (cachedMatches.isNotEmpty()) {
                    emit(Resource.Success(
                        data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                } else {
                    println("🟡 DEBUG: No cached data available, using mock data for today's matches")
                    mockDataProvider.setUsingMockData(true)
                    emit(Resource.Success(
                        data = mockDataProvider.getMockTodayMatches()
                    ))
                }
            }
        } catch (e: Exception) {
            val errorMsg = "Exception in getTodayMatches: ${e.localizedMessage}"
            println("🔴 DEBUG: $errorMsg")
            e.printStackTrace()

            // Use mock data for network issues or when no cached data available
            val cachedData = matchDao.getTodayMatchesList().map { MatchEntityMapper.mapEntityToDomain(it) }
            if (mockDataProvider.shouldUseMockDataForException(e) || cachedData.isEmpty()) {
                println("🟡 DEBUG: Network issue detected or no cached data, using mock today's matches")
                mockDataProvider.setUsingMockData(true)
                emit(Resource.Success(
                    data = mockDataProvider.getMockTodayMatches()
                ))
            } else {
                emit(Resource.Error(
                    message = errorMsg,
                    data = cachedData
                ))
            }
        }
    }

    override fun getMatchesForDate(date: String): Flow<Resource<List<Match>>> = flow {
        emit(Resource.Loading())

        try {
            // Get cached data first (we'll need to add a suspend method to DAO)
            // For now, skip cached data for this method and fetch directly from API

            // Fetch fresh data from API
            val response = apiService.getTodayFixtures(date)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                    matchDao.insertMatches(entities)

                    emit(Resource.Success(
                        data = entities.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                }
            } else {
                emit(Resource.Error(
                    message = "Failed to fetch matches for date $date: ${response.message()}"
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred"
            ))
        }
    }

    override fun getMatchById(matchId: Int): Flow<Resource<Match>> = flow {
        emit(Resource.Loading())

        // Get cached data first
        val cachedMatch = matchDao.getMatchById(matchId)
        cachedMatch?.let {
            emit(Resource.Success(
                data = MatchEntityMapper.mapEntityToDomain(it)
            ))
        }

        try {
            // Fetch fresh data from API
            val response = apiService.getMatchDetails(matchId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val match = apiResponse.response.firstOrNull()
                    if (match != null) {
                        val entity = MatchMapper.mapDtoToEntity(match)
                        matchDao.insertMatch(entity)

                        emit(Resource.Success(
                            data = MatchEntityMapper.mapEntityToDomain(entity)
                        ))
                    }
                }
            } else if (cachedMatch == null) {
                emit(Resource.Error(
                    message = "Failed to fetch match details: ${response.message()}"
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred",
                data = cachedMatch?.let { MatchEntityMapper.mapEntityToDomain(it) }
            ))
        }
    }

    override fun getTeamMatches(teamId: Int, season: Int): Flow<Resource<List<Match>>> = flow {
        emit(Resource.Loading())

        try {
            // Fetch fresh data from API
            val response = apiService.getTeamFixtures(teamId, season)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                    matchDao.insertMatches(entities)

                    emit(Resource.Success(
                        data = entities.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                }
            } else {
                emit(Resource.Error(
                    message = "Failed to fetch team matches: ${response.message()}"
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred"
            ))
        }
    }

    override fun getLeagueMatches(leagueId: Int, season: Int): Flow<Resource<List<Match>>> = flow {
        emit(Resource.Loading())

        try {
            // Fetch fresh data from API
            val response = apiService.getLeagueFixtures(leagueId, season)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                    matchDao.insertMatches(entities)

                    emit(Resource.Success(
                        data = entities.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                }
            } else {
                emit(Resource.Error(
                    message = "Failed to fetch league matches: ${response.message()}"
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred"
            ))
        }
    }

    override suspend fun subscribeToLiveMatch(matchId: Int) {
        liveMatchService.subscribeToMatch(matchId)
    }

    override suspend fun unsubscribeFromLiveMatch(matchId: Int) {
        liveMatchService.unsubscribeFromMatch(matchId)
    }

    override fun searchMatches(query: String): Flow<Resource<List<Match>>> = flow {
        emit(Resource.Loading())

        try {
            val matches = matchDao.searchMatches(query)
            emit(Resource.Success(
                data = matches.map { MatchEntityMapper.mapEntityToDomain(it) }
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred"
            ))
        }
    }
}