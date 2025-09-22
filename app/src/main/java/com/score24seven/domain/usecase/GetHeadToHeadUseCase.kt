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

class GetHeadToHeadUseCase @Inject constructor(
    private val apiService: FootballApiService
) {
    operator fun invoke(fixtureId: Int): Flow<Resource<HeadToHeadData>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchPredictions(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val responseList = apiResponse.response ?: emptyList()
                    if (responseList.isNotEmpty()) {
                        val predictionData = responseList.first()
                        val h2hData = HeadToHeadData(
                            predictions = if (predictionData.predictions.winner != null) {
                                MatchPrediction(
                                    winner = predictionData.predictions.winner.name,
                                    winnerComment = predictionData.predictions.winner.comment,
                                    advice = predictionData.predictions.advice,
                                    homePercent = predictionData.predictions.percent?.home,
                                    drawPercent = predictionData.predictions.percent?.draw,
                                    awayPercent = predictionData.predictions.percent?.away
                                )
                            } else null,
                            recentMatches = predictionData.h2h.map { match ->
                                RecentMatch(
                                    id = match.fixture.id,
                                    date = match.fixture.date.toString(),
                                    homeTeam = match.teams.home.name,
                                    awayTeam = match.teams.away.name,
                                    homeScore = match.goals?.home ?: 0,
                                    awayScore = match.goals?.away ?: 0,
                                    league = match.league.name
                                )
                            }
                        )
                        emit(Resource.Success(h2hData))
                    } else {
                        emit(Resource.Error("No head-to-head data available"))
                    }
                } ?: emit(Resource.Error("No head-to-head data"))
            } else {
                emit(Resource.Error("Failed to fetch head-to-head: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}

data class HeadToHeadData(
    val predictions: MatchPrediction?,
    val recentMatches: List<RecentMatch>
)

data class MatchPrediction(
    val winner: String?,
    val winnerComment: String?,
    val advice: String?,
    val homePercent: String?,
    val drawPercent: String?,
    val awayPercent: String?
)

data class RecentMatch(
    val id: Int,
    val date: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val league: String
)