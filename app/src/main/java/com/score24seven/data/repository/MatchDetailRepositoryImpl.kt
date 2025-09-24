/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.repository

import com.score24seven.data.api.FootballApiService
import com.score24seven.data.api.PredictionDto
import com.score24seven.data.database.dao.MatchDao
import com.score24seven.data.dto.*
import com.score24seven.data.database.entity.*
import com.score24seven.data.mapper.*
import com.score24seven.data.api.EventDto as ApiEventDto
import com.score24seven.data.api.LineupDto as ApiLineupDto
import com.score24seven.data.api.StatisticsDto as ApiStatisticsDto
import com.score24seven.domain.model.*
import com.score24seven.domain.repository.MatchDetailRepository
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchDetailRepositoryImpl @Inject constructor(
    private val apiService: FootballApiService,
    private val matchDao: MatchDao
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

    override fun getMatchLineups(fixtureId: Int): Flow<Resource<List<Lineup>>> = flow {
        emit(Resource.Loading())

        try {
            // First, try to get from database
            val cachedLineups = matchDao.getMatchLineupsList(fixtureId)
            if (cachedLineups.isNotEmpty()) {
                val domainLineups = cachedLineups.toDomainLineups()
                emit(Resource.Success(data = domainLineups))
            }

            // Fetch fresh data from API
            val response = apiService.getMatchLineups(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val lineupDtos: List<ApiLineupDto> = apiResponse.response ?: emptyList()

                    // Convert to domain models
                    val domainLineups: List<Lineup> = lineupDtos.map { lineupDto ->
                        lineupDto.toDomain()
                    }

                    // Cache the lineups
                    val lineupEntities: List<MatchLineupEntity> = lineupDtos.flatMap { lineupDto ->
                        lineupDto.toEntities(fixtureId)
                    }
                    matchDao.insertMatchLineups(lineupEntities)
                    matchDao.updateMatchLineupsFlag(fixtureId, lineupDtos.isNotEmpty())

                    emit(Resource.Success(data = domainLineups))
                } ?: run {
                    emit(Resource.Error(message = "No lineups data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load lineups"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getMatchStatistics(fixtureId: Int): Flow<Resource<List<MatchStatistic>>> = flow {
        emit(Resource.Loading())

        try {
            // First, try to get from database
            val cachedStats = matchDao.getMatchStatisticsList(fixtureId)
            if (cachedStats.isNotEmpty()) {
                val domainStats = cachedStats.toDomainStatistics()
                emit(Resource.Success(data = domainStats))
            }

            // Fetch fresh data from API
            val response = apiService.getMatchStatistics(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val statisticsDtos: List<ApiStatisticsDto> = apiResponse.response ?: emptyList()

                    // Convert to domain models
                    val domainStats: List<MatchStatistic> = if (statisticsDtos.size >= 2) {
                        statisticsDtos[0].toDomain(statisticsDtos[1])
                    } else if (statisticsDtos.isNotEmpty()) {
                        statisticsDtos[0].toDomain(null)
                    } else {
                        emptyList<MatchStatistic>()
                    }

                    // Cache the statistics
                    val statEntities: List<MatchStatisticsEntity> = statisticsDtos.flatMap { statsDto ->
                        statsDto.toEntities(fixtureId)
                    }
                    matchDao.insertMatchStatistics(statEntities)
                    matchDao.updateMatchStatisticsFlag(fixtureId, statisticsDtos.isNotEmpty())

                    emit(Resource.Success(data = domainStats))
                } ?: run {
                    emit(Resource.Error(message = "No statistics data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load statistics"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getMatchEvents(fixtureId: Int): Flow<Resource<List<MatchEvent>>> = flow {
        emit(Resource.Loading())

        try {
            // First, try to get from database
            val cachedEvents = matchDao.getMatchEventsList(fixtureId)
            if (cachedEvents.isNotEmpty()) {
                val domainEvents = cachedEvents.map { it.toDomain() }
                emit(Resource.Success(data = domainEvents))
            }

            // Fetch fresh data from API
            val response = apiService.getMatchEvents(fixtureId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val eventDtos: List<ApiEventDto> = apiResponse.response ?: emptyList()

                    // Convert to domain models
                    val domainEvents: List<MatchEvent> = eventDtos.map { eventDto ->
                        eventDto.toDomain()
                    }

                    // Cache the events
                    val eventEntities: List<MatchEventEntity> = eventDtos.map { eventDto ->
                        eventDto.toEntity(fixtureId)
                    }
                    matchDao.insertMatchEvents(eventEntities)
                    matchDao.updateMatchEventsFlag(fixtureId, eventDtos.isNotEmpty())

                    emit(Resource.Success(data = domainEvents))
                } ?: run {
                    emit(Resource.Error(message = "No events data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load events"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getHeadToHead(homeTeamId: Int, awayTeamId: Int): Flow<Resource<List<Match>>> = flow {
        emit(Resource.Loading())

        try {
            // First, try to get from database cache
            val cachedMatches = matchDao.getHeadToHeadMatches(homeTeamId, awayTeamId, 10)
            if (cachedMatches.isNotEmpty()) {
                // Convert to domain - need to implement MatchEntity to Match mapper
                // emit(Resource.Success(data = cachedMatches.map { it.toDomain() }))
            }

            // Fetch fresh data from API
            val response = apiService.getHeadToHead("$homeTeamId-$awayTeamId")
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val matchDtos: List<MatchDto> = apiResponse.response ?: emptyList()

                    // Convert to domain models using MatchDto to Match mapper
                    val domainMatches: List<Match> = matchDtos.map { matchDto ->
                        matchDto.toDomain()
                    }

                    emit(Resource.Success(data = domainMatches))
                } ?: run {
                    emit(Resource.Error(message = "No head-to-head data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load head-to-head data"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getLeagueMatches(leagueId: Int, season: Int): Flow<Resource<List<Match>>> = flow {
        emit(Resource.Loading())

        try {
            // First, try to get from database
            val cachedMatches = matchDao.getLeagueMatches(leagueId, season)
            cachedMatches.map { matches ->
                if (matches.isNotEmpty()) {
                    // Convert to domain - need to implement MatchEntity to Match mapper
                    // emit(Resource.Success(data = matches.map { it.toDomain() }))
                }
            }

            // Fetch fresh data from API
            val response = apiService.getLeagueFixtures(leagueId, season)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val matchDtos: List<MatchDto> = apiResponse.response ?: emptyList()

                    // Convert to domain models using existing MatchDto to Match mapper
                    val domainMatches: List<Match> = matchDtos.map { matchDto ->
                        matchDto.toDomain()
                    }

                    emit(Resource.Success(data = domainMatches))
                } ?: run {
                    emit(Resource.Error(message = "No league matches data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load league matches"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getLeagueStandings(leagueId: Int, season: Int): Flow<Resource<List<Standing>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getLeagueStandings(leagueId, season)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val standings = apiResponse.response ?: emptyList()

                    // Convert to domain models with null safety
                    val domainStandings = standings.flatMap { standingResponse ->
                        standingResponse.standings?.flatMap { standingGroup ->
                            standingGroup?.map { standingDto ->
                                Standing(
                                    rank = standingDto.rank,
                                    team = Team(
                                        id = standingDto.team.id,
                                        name = standingDto.team.name,
                                        logo = standingDto.team.logo
                                    ),
                                    points = standingDto.points,
                                    goalsDiff = standingDto.goalsDiff,
                                    form = standingDto.form,
                                    status = standingDto.status,
                                    description = standingDto.description,
                                    update = standingDto.update,
                                    all = StandingStats(
                                        played = standingDto.all.played,
                                        win = standingDto.all.win,
                                        draw = standingDto.all.draw,
                                        lose = standingDto.all.lose,
                                        goals = StandingGoals(
                                            goalsFor = standingDto.all.goals.goalsAgainst + standingDto.goalsDiff,
                                            goalsAgainst = standingDto.all.goals.goalsAgainst
                                        )
                                    )
                                )
                            } ?: emptyList()
                        } ?: emptyList()
                    }

                    emit(Resource.Success(data = domainStandings))
                } ?: run {
                    emit(Resource.Error(message = "No standings data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load standings"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getTeamFixtures(teamId: Int, season: Int, limit: Int): Flow<Resource<List<Match>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getTeamFixtures(teamId, season, last = limit)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val matchDtos: List<MatchDto> = apiResponse.response ?: emptyList()

                    // Convert to domain models using existing MatchDto to Match mapper
                    val domainMatches: List<Match> = matchDtos.map { matchDto ->
                        matchDto.toDomain()
                    }

                    emit(Resource.Success(data = domainMatches))
                } ?: run {
                    emit(Resource.Error(message = "No team fixtures data available"))
                }
            } else {
                emit(Resource.Error(message = "Unable to load team fixtures"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}