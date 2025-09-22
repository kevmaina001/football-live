/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.repository

import com.score24seven.data.api.FootballApiService
import com.score24seven.data.mapper.TopScorerMapper
import com.score24seven.domain.model.TopScorer
import com.score24seven.domain.repository.TopScorerRepository
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopScorerRepositoryImpl @Inject constructor(
    private val apiService: FootballApiService
) : TopScorerRepository {

    override fun getTopScorers(leagueId: Int, season: Int): Flow<Resource<List<TopScorer>>> = flow {
        println("DEBUG: TopScorerRepositoryImpl - getTopScorers called with leagueId: $leagueId, season: $season")
        emit(Resource.Loading())

        try {
            val response = apiService.getTopScorers(leagueId, season)
            println("DEBUG: TopScorerRepositoryImpl - API response successful: ${response.isSuccessful}")
            println("DEBUG: TopScorerRepositoryImpl - API response code: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    println("DEBUG: TopScorerRepositoryImpl - API response contains ${(apiResponse.response ?: emptyList()).size} top scorers")

                    val topScorers = TopScorerMapper.mapPlayerDtosToDomain(apiResponse.response ?: emptyList())
                    println("DEBUG: TopScorerRepositoryImpl - Mapped ${topScorers.size} top scorers")
                    emit(Resource.Success(data = topScorers))
                } ?: run {
                    println("DEBUG: TopScorerRepositoryImpl - API response body is null")
                    emit(Resource.Error(message = "No top scorers data received"))
                }
            } else {
                println("DEBUG: TopScorerRepositoryImpl - API call failed: ${response.message()}")
                emit(Resource.Error(message = "Failed to fetch top scorers: ${response.message()}"))
            }
        } catch (e: Exception) {
            println("DEBUG: TopScorerRepositoryImpl - Exception: ${e.message}")
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}