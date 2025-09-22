/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.usecase

import com.score24seven.data.api.FootballApiService
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMatchStatisticsUseCase @Inject constructor(
    private val apiService: FootballApiService
) {
    operator fun invoke(fixtureId: Int): Flow<Resource<List<TeamStatistics>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchStatistics(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val stats = (apiResponse.response ?: emptyList()).map { statsDto ->
                        TeamStatistics(
                            teamId = statsDto.team.id,
                            teamName = statsDto.team.name,
                            teamLogo = statsDto.team.logo,
                            statistics = statsDto.statistics.associate { stat ->
                                stat.type to (stat.value ?: "0")
                            }
                        )
                    }
                    emit(Resource.Success(stats))
                } ?: emit(Resource.Error("No statistics data"))
            } else {
                emit(Resource.Error("Failed to fetch statistics: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}

data class TeamStatistics(
    val teamId: Int,
    val teamName: String,
    val teamLogo: String?,
    val statistics: Map<String, String>
)