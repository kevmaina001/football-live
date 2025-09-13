/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kickscore.live.data.api.FootballApiService
import com.kickscore.live.data.database.KickScoreDatabase
import com.kickscore.live.data.mapper.MatchMapper
import com.kickscore.live.data.paging.MatchRemoteMediator
import com.kickscore.live.data.websocket.LiveMatchService
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.repository.MatchRepository
import com.kickscore.live.domain.util.Resource
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
    private val database: KickScoreDatabase,
    private val liveMatchService: LiveMatchService
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
        emit(Resource.Loading())

        try {
            // Emit cached data first
            val cachedMatches = matchDao.getLiveMatchesList()
            if (cachedMatches.isNotEmpty()) {
                emit(Resource.Success(
                    data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                ))
            }

            // Fetch fresh data from API
            val response = apiService.getLiveMatches()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                    matchDao.refreshLiveMatches(entities)

                    emit(Resource.Success(
                        data = entities.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                }
            } else {
                emit(Resource.Error(
                    message = "Failed to fetch live matches: ${response.message()}",
                    data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred",
                data = matchDao.getLiveMatchesList().map { MatchEntityMapper.mapEntityToDomain(it) }
            ))
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
        emit(Resource.Loading())

        try {
            // Get today's date
            val today = LocalDateTime.now().toLocalDate().atStartOfDay()
            val todayString = today.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)

            // Emit cached data first
            matchDao.getTodayMatches().collect { cachedMatches ->
                if (cachedMatches.isNotEmpty()) {
                    emit(Resource.Success(
                        data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                }
            }

            // Fetch fresh data from API
            val response = apiService.getTodayFixtures(todayString)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                    matchDao.insertMatches(entities)
                }
            } else {
                emit(Resource.Error(
                    message = "Failed to fetch today's matches: ${response.message()}"
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

        try {
            // Emit cached data first
            val cachedMatch = matchDao.getMatchById(matchId)
            cachedMatch?.let {
                emit(Resource.Success(
                    data = MatchEntityMapper.mapEntityToDomain(it)
                ))
            }

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
            // Emit cached data first
            matchDao.getTeamMatches(teamId, season).collect { cachedMatches ->
                if (cachedMatches.isNotEmpty()) {
                    emit(Resource.Success(
                        data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                }
            }

            // Fetch fresh data from API
            val response = apiService.getTeamFixtures(teamId, season)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                    matchDao.insertMatches(entities)
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
            // Emit cached data first
            matchDao.getLeagueMatches(leagueId, season).collect { cachedMatches ->
                if (cachedMatches.isNotEmpty()) {
                    emit(Resource.Success(
                        data = cachedMatches.map { MatchEntityMapper.mapEntityToDomain(it) }
                    ))
                }
            }

            // Fetch fresh data from API
            val response = apiService.getLeagueFixtures(leagueId, season)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val entities = MatchMapper.mapDtosToEntities(apiResponse.response)
                    matchDao.insertMatches(entities)
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