/*
 * League info repository implementation following friend's working approach
 */

package com.kickscore.live.data.repository

import com.kickscore.live.data.api.FootballApiService
import com.kickscore.live.data.mapper.LeagueInfoMapper
import com.kickscore.live.domain.model.LeagueInfo
import com.kickscore.live.domain.repository.LeagueInfoRepository
import com.kickscore.live.domain.util.Resource
import com.kickscore.live.util.Config
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeagueInfoRepositoryImpl @Inject constructor(
    private val apiService: FootballApiService
) : LeagueInfoRepository {

    // Use Config.PRIORITY_LEAGUE_IDS directly
    private val priorityLeagueIds = Config.PRIORITY_LEAGUE_IDS

    override fun getCurrentLeagues(): Flow<Resource<List<LeagueInfo>>> = flow {
        println("🔵 DEBUG: LeagueInfoRepositoryImpl - getCurrentLeagues called")
        emit(Resource.Loading())

        try {
            println("🔵 DEBUG: LeagueInfoRepositoryImpl - Making API call to getLeagues(current=true)")
            val response = apiService.getLeagues(current = true)
            println("🔵 DEBUG: LeagueInfoRepositoryImpl - API response successful: ${response.isSuccessful}")
            println("🔵 DEBUG: LeagueInfoRepositoryImpl - API response code: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - API response body exists")
                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - API response errors: ${apiResponse.errors}")
                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - API response results: ${apiResponse.results}")

                    val responseList = apiResponse.response
                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - API response contains ${responseList.size} leagues")

                    if (responseList.isNotEmpty()) {
                        println("🔵 DEBUG: LeagueInfoRepositoryImpl - First league: ${responseList[0].league.name} (ID: ${responseList[0].league.id})")
                        println("🔵 DEBUG: LeagueInfoRepositoryImpl - First league logo: ${responseList[0].league.logo}")
                        println("🔵 DEBUG: LeagueInfoRepositoryImpl - First country: ${responseList[0].country.name}")
                        println("🔵 DEBUG: LeagueInfoRepositoryImpl - First country flag: ${responseList[0].country.flag}")
                        println("🔵 DEBUG: LeagueInfoRepositoryImpl - First country code: ${responseList[0].country.code}")

                        // Log first few leagues for debugging
                        responseList.take(5).forEach { leagueDto ->
                            println("🏳️ DEBUG: League ${leagueDto.league.name} | Country: ${leagueDto.country.name} | Flag URL: '${leagueDto.country.flag}'")
                        }
                    }

                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - Starting mapping...")
                    val allLeagues = LeagueInfoMapper.mapDtosToDomain(responseList)
                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - Mapping completed, ${allLeagues.size} leagues mapped")

                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - Starting filtering...")
                    val filteredLeagues = createFilteredList(allLeagues)
                    println("🔵 DEBUG: LeagueInfoRepositoryImpl - Filtering completed, ${filteredLeagues.size} leagues filtered")

                    emit(Resource.Success(data = filteredLeagues))
                } ?: run {
                    println("🔴 DEBUG: LeagueInfoRepositoryImpl - API response body is null")
                    emit(Resource.Error(message = "No leagues data received"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                println("🔴 DEBUG: LeagueInfoRepositoryImpl - API call failed: ${response.code()} - ${response.message()}")
                println("🔴 DEBUG: LeagueInfoRepositoryImpl - Error body: $errorBody")
                println("🔴 DEBUG: LeagueInfoRepositoryImpl - Request URL: ${response.raw().request.url}")
                emit(Resource.Error(message = "API Error ${response.code()}: ${response.message()}. Error: $errorBody"))
            }
        } catch (e: Exception) {
            println("🔴 DEBUG: LeagueInfoRepositoryImpl - Exception: ${e.javaClass.simpleName}: ${e.message}")
            println("🔴 DEBUG: LeagueInfoRepositoryImpl - Exception stacktrace:")
            e.printStackTrace()
            val detailedMessage = when (e) {
                is java.net.UnknownHostException -> "Network error: Unable to reach server. Check internet connection."
                is java.net.SocketTimeoutException -> "Request timeout: Server took too long to respond."
                is com.google.gson.JsonSyntaxException -> "JSON parsing error: ${e.message}"
                is retrofit2.HttpException -> "HTTP error ${e.code()}: ${e.message()}"
                else -> "Unknown error: ${e.javaClass.simpleName} - ${e.localizedMessage ?: e.message}"
            }
            emit(Resource.Error(message = detailedMessage))
        }
    }

    override fun searchLeagues(query: String): Flow<Resource<List<LeagueInfo>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getLeagues(current = true)

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val responseList = apiResponse.response
                    val allLeagues = LeagueInfoMapper.mapDtosToDomain(responseList)
                    val filteredLeagues = allLeagues.filter { league ->
                        (league.league.name.lowercase() + league.country.name.lowercase())
                            .contains(query.lowercase())
                    }
                    emit(Resource.Success(data = filteredLeagues))
                } ?: run {
                    emit(Resource.Error(message = "No leagues data received"))
                }
            } else {
                emit(Resource.Error(message = "Failed to search leagues: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = "Error searching leagues: ${e.localizedMessage}"))
        }
    }

    // Friend's exact filtering approach
    private fun createFilteredList(allLeagues: List<LeagueInfo>): List<LeagueInfo> {
        println("🔵 DEBUG: LeagueInfoRepositoryImpl - createFilteredList called with ${allLeagues.size} leagues")

        val filteredList = mutableListOf<LeagueInfo>()
        val remainingLeagues = allLeagues.toMutableList()

        println("🔵 DEBUG: LeagueInfoRepositoryImpl - Priority league IDs: ${priorityLeagueIds.joinToString()}")

        // Add priority leagues first (same order as friend's Config.leagueId)
        for (priorityId in priorityLeagueIds) {
            val league = findAndRemoveLeague(remainingLeagues, priorityId)
            if (league != null) {
                println("🔵 DEBUG: LeagueInfoRepositoryImpl - Found priority league: ${league.league.name} (ID: $priorityId)")
                filteredList.add(league)
            } else {
                println("🔵 DEBUG: LeagueInfoRepositoryImpl - Priority league ID $priorityId not found")
            }
        }

        println("🔵 DEBUG: LeagueInfoRepositoryImpl - Found ${filteredList.size} priority leagues")

        // Add remaining leagues
        filteredList.addAll(remainingLeagues)
        println("🔵 DEBUG: LeagueInfoRepositoryImpl - Final filtered list has ${filteredList.size} leagues")

        return filteredList
    }

    private fun findAndRemoveLeague(leagues: MutableList<LeagueInfo>, leagueId: Int): LeagueInfo? {
        val league = leagues.find { it.league.id == leagueId }
        if (league != null) {
            leagues.remove(league)
        }
        return league
    }
}