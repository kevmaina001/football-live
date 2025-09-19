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

class GetMatchLineupsUseCase @Inject constructor(
    private val apiService: FootballApiService
) {
    operator fun invoke(fixtureId: Int): Flow<Resource<List<TeamLineup>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getMatchLineups(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val lineups = (apiResponse.response ?: emptyList()).map { lineupDto ->
                        TeamLineup(
                            teamId = lineupDto.team.id,
                            teamName = lineupDto.team.name,
                            teamLogo = lineupDto.team.logo,
                            formation = lineupDto.formation,
                            coach = lineupDto.coach.name,
                            startingXI = lineupDto.startXI.map { startXI ->
                                LineupPlayer(
                                    id = startXI.player.id,
                                    name = startXI.player.name,
                                    number = startXI.player.number,
                                    position = startXI.player.pos,
                                    grid = startXI.player.grid
                                )
                            },
                            substitutes = lineupDto.substitutes.map { sub ->
                                LineupPlayer(
                                    id = sub.player.id,
                                    name = sub.player.name,
                                    number = sub.player.number,
                                    position = sub.player.pos,
                                    grid = sub.player.grid
                                )
                            }
                        )
                    }
                    emit(Resource.Success(lineups))
                } ?: emit(Resource.Error("No lineup data"))
            } else {
                emit(Resource.Error("Failed to fetch lineups: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}

data class TeamLineup(
    val teamId: Int,
    val teamName: String,
    val teamLogo: String?,
    val formation: String,
    val coach: String,
    val startingXI: List<LineupPlayer>,
    val substitutes: List<LineupPlayer>
)

data class LineupPlayer(
    val id: Int,
    val name: String,
    val number: Int,
    val position: String,
    val grid: String?
)