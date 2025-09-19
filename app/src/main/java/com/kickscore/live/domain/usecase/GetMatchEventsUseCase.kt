/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.usecase

import com.kickscore.live.data.api.FootballApiService
import com.kickscore.live.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMatchEventsUseCase @Inject constructor(
    private val apiService: FootballApiService
) {
    operator fun invoke(fixtureId: Int): Flow<Resource<List<MatchEvent>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchEvents(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val events = (apiResponse.response ?: emptyList()).map { eventDto ->
                        MatchEvent(
                            minute = eventDto.time.elapsed,
                            extraTime = eventDto.time.extra,
                            type = eventDto.type,
                            detail = eventDto.detail,
                            player = eventDto.player.name,
                            assist = eventDto.assist?.name,
                            team = eventDto.team.name,
                            comments = eventDto.comments
                        )
                    }
                    emit(Resource.Success(events))
                } ?: emit(Resource.Error("No events data"))
            } else {
                emit(Resource.Error("Failed to fetch events: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}

data class MatchEvent(
    val minute: Int,
    val extraTime: Int?,
    val type: String,
    val detail: String,
    val player: String,
    val assist: String?,
    val team: String,
    val comments: String?
)