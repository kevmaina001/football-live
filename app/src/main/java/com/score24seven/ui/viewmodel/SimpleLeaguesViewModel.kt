/*
 * Simple leagues ViewModel using Volley API following working pattern
 */

package com.score24seven.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.data.api.VolleyApiService
import com.score24seven.data.model.SimpleLeagueItem
import com.score24seven.data.model.SimpleStanding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SimpleLeaguesViewModel(application: Application) : AndroidViewModel(application) {

    private val volleyService = VolleyApiService(application)

    private val _uiState = MutableStateFlow(SimpleLeaguesUiState())
    val uiState: StateFlow<SimpleLeaguesUiState> = _uiState.asStateFlow()

    init {
        loadLeagues()
    }

    fun loadLeagues() {
        println("🔥 DEBUG: SimpleLeaguesViewModel - Loading leagues")
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        volleyService.getCurrentLeagues(
            onSuccess = { response ->
                viewModelScope.launch {
                    val leagues = response.response ?: emptyList()
                    println("🔥 DEBUG: Total leagues from API: ${leagues.size}")
                    leagues.take(5).forEach { league ->
                        println("🔥 DEBUG: League ${league.league?.name} (ID: ${league.league?.id})")
                        league.seasons?.take(3)?.forEach { season ->
                            println("🔥 DEBUG: - Season ${season.year}, current: ${season.current}, standings: ${season.coverage?.standings}")
                        }
                    }

                    val priorityLeagues = leagues.filter { league ->
                        league.league?.id in listOf(39, 2, 3, 140, 78, 135, 61, 1, 4, 5) // Popular leagues
                    }

                    val otherLeagues = leagues.filter { league ->
                        league.league?.id !in listOf(39, 2, 3, 140, 78, 135, 61, 1, 4, 5) &&
                        league.seasons?.any { it.current == true } == true
                    }.take(15) // Limit to prevent overwhelming

                    println("🔥 DEBUG: SimpleLeaguesViewModel - Loaded ${leagues.size} total, ${priorityLeagues.size} priority, ${otherLeagues.size} other")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        priorityLeagues = priorityLeagues,
                        otherLeagues = otherLeagues,
                        errorMessage = null
                    )
                }
            },
            onError = { error ->
                viewModelScope.launch {
                    println("🔴 DEBUG: SimpleLeaguesViewModel - Error: $error")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error
                    )
                }
            }
        )
    }

    fun loadStandings(leagueId: Int, season: Int, leagueName: String) {
        // Use current year if season seems invalid
        val actualSeason = if (season in 2020..2025) season else 2024
        println("🏆 DEBUG: SimpleLeaguesViewModel - Loading standings for league $leagueId, season $actualSeason (original: $season)")
        _uiState.value = _uiState.value.copy(
            standingsLoading = true,
            standingsError = null,
            selectedLeague = Triple(leagueId, actualSeason, leagueName)
        )

        volleyService.getLeagueStandings(
            leagueId = leagueId,
            season = actualSeason,
            onSuccess = { response ->
                viewModelScope.launch {
                    println("🏆 DEBUG: SimpleLeaguesViewModel - Processing response...")

                    val allStandings = mutableListOf<SimpleStanding>()
                    response.response?.forEach { standingResponse ->
                        println("🏆 DEBUG: Processing league: ${standingResponse.league?.name}")
                        standingResponse.league?.standings?.forEach { standingGroup ->
                            println("🏆 DEBUG: Standing group size: ${standingGroup.size}")
                            allStandings.addAll(standingGroup)
                        }
                    }

                    println("🏆 DEBUG: SimpleLeaguesViewModel - Total standings collected: ${allStandings.size}")
                    allStandings.take(5).forEach { standing ->
                        println("🏆 DEBUG: Standing - ${standing.rank}. ${standing.team?.name} (${standing.points} pts)")
                    }

                    _uiState.value = _uiState.value.copy(
                        standingsLoading = false,
                        standings = allStandings,
                        standingsError = if (allStandings.isEmpty()) "No standings available for this league" else null,
                        showStandings = true
                    )
                }
            },
            onError = { error ->
                viewModelScope.launch {
                    println("🔴 DEBUG: SimpleLeaguesViewModel - Standings error: $error")
                    _uiState.value = _uiState.value.copy(
                        standingsLoading = false,
                        standingsError = error
                    )
                }
            }
        )
    }

    fun clearStandings() {
        _uiState.value = _uiState.value.copy(
            showStandings = false,
            standings = emptyList(),
            selectedLeague = null,
            standingsError = null
        )
    }

    fun retry() {
        if (_uiState.value.showStandings && _uiState.value.selectedLeague != null) {
            val (leagueId, season, leagueName) = _uiState.value.selectedLeague!!
            loadStandings(leagueId, season, leagueName)
        } else {
            loadLeagues()
        }
    }
}

data class SimpleLeaguesUiState(
    val isLoading: Boolean = false,
    val priorityLeagues: List<SimpleLeagueItem> = emptyList(),
    val otherLeagues: List<SimpleLeagueItem> = emptyList(),
    val errorMessage: String? = null,
    val showStandings: Boolean = false,
    val standingsLoading: Boolean = false,
    val standings: List<SimpleStanding> = emptyList(),
    val standingsError: String? = null,
    val selectedLeague: Triple<Int, Int, String>? = null // leagueId, season, name
)