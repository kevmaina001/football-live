/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.repository

import com.score24seven.data.api.FootballApiService
import com.score24seven.data.api.EventDto
import com.score24seven.data.api.LineupDto
import com.score24seven.data.api.PredictionDto
import com.score24seven.data.api.StatisticsDto
import com.score24seven.data.dto.MatchDto
import com.score24seven.domain.repository.MatchDetailRepository
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchDetailRepositoryImpl @Inject constructor(
    private val apiService: FootballApiService
) : MatchDetailRepository {

    override fun getMatchPredictions(fixtureId: Int): Flow<Resource<List<PredictionDto>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchPredictions(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    emit(Resource.Success(data = apiResponse.response ?: emptyList()))
                } ?: run {
                    emit(Resource.Error(message = "No data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load data"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getMatchLineups(fixtureId: Int): Flow<Resource<List<LineupDto>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchLineups(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    emit(Resource.Success(data = apiResponse.response ?: emptyList()))
                } ?: run {
                    emit(Resource.Error(message = "No data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load data"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getMatchStatistics(fixtureId: Int): Flow<Resource<List<StatisticsDto>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchStatistics(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    emit(Resource.Success(data = apiResponse.response ?: emptyList()))
                } ?: run {
                    emit(Resource.Error(message = "No data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load data"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getMatchEvents(fixtureId: Int): Flow<Resource<List<EventDto>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchEvents(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    emit(Resource.Success(data = apiResponse.response ?: emptyList()))
                } ?: run {
                    emit(Resource.Error(message = "No data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load data"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getHeadToHead(homeTeamId: Int, awayTeamId: Int): Flow<Resource<List<MatchDto>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getHeadToHead("$homeTeamId-$awayTeamId")
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    emit(Resource.Success(data = apiResponse.response ?: emptyList()))
                } ?: run {
                    emit(Resource.Error(message = "No data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load data"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getLeagueMatches(leagueId: Int, season: Int): Flow<Resource<List<MatchDto>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getLeagueFixtures(leagueId, season)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    emit(Resource.Success(data = apiResponse.response ?: emptyList()))
                } ?: run {
                    emit(Resource.Error(message = "No data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load data"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}