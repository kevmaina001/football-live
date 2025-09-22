/*
 * League Details ViewModel with comprehensive 4-tab data management
 * Handles Matches, Standing, Prediction, Top Scorer data
 */

package com.score24seven.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.data.api.VolleyApiService
import com.score24seven.data.model.SimpleStanding
import com.score24seven.data.model.SimpleFixture
import com.score24seven.data.model.SimpleTopScorer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeagueDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val volleyService = VolleyApiService(application)

    private val _uiState = MutableStateFlow(LeagueDetailsUiState())
    val uiState: StateFlow<LeagueDetailsUiState> = _uiState.asStateFlow()

    fun loadLeagueDetails(leagueId: Int, season: Int, leagueName: String) {
        println("🏆 DEBUG: LeagueDetailsViewModel - Loading details for league $leagueId, season $season")
        _uiState.value = _uiState.value.copy(
            leagueId = leagueId,
            season = season,
            leagueName = leagueName
        )

        // Load standings, fixtures, and top scorers
        loadStandings(leagueId, season)
        loadFixtures(leagueId, season)
        loadTopScorers(leagueId, season)
    }

    private fun loadStandings(leagueId: Int, season: Int) {
        println("🏆 DEBUG: LeagueDetailsViewModel - Loading standings for league $leagueId, season $season")
        _uiState.value = _uiState.value.copy(
            standingsLoading = true,
            standingsError = null
        )

        volleyService.getLeagueStandings(
            leagueId = leagueId,
            season = season,
            onSuccess = { response ->
                viewModelScope.launch {
                    println("🏆 DEBUG: LeagueDetailsViewModel - Processing standings response...")

                    val allStandings = mutableListOf<SimpleStanding>()
                    response.response?.forEach { standingResponse ->
                        println("🏆 DEBUG: Processing league: ${standingResponse.league?.name}")
                        standingResponse.league?.standings?.forEach { standingGroup ->
                            println("🏆 DEBUG: Standing group size: ${standingGroup.size}")
                            allStandings.addAll(standingGroup)
                        }
                    }

                    println("🏆 DEBUG: LeagueDetailsViewModel - Total standings collected: ${allStandings.size}")

                    _uiState.value = _uiState.value.copy(
                        standingsLoading = false,
                        standings = allStandings,
                        standingsError = if (allStandings.isEmpty()) "No standings available for this league" else null
                    )
                }
            },
            onError = { error ->
                viewModelScope.launch {
                    println("🔴 DEBUG: LeagueDetailsViewModel - Standings error: $error")
                    _uiState.value = _uiState.value.copy(
                        standingsLoading = false,
                        standingsError = error
                    )
                }
            }
        )
    }

    private fun loadFixtures(leagueId: Int, season: Int) {
        println("⚽ DEBUG: LeagueDetailsViewModel - Loading fixtures for league $leagueId, season $season")
        _uiState.value = _uiState.value.copy(
            fixturesLoading = true,
            fixturesError = null
        )

        volleyService.getLeagueFixtures(
            leagueId = leagueId,
            season = season,
            onSuccess = { response ->
                viewModelScope.launch {
                    println("⚽ DEBUG: LeagueDetailsViewModel - Processing fixtures response...")
                    val fixtures = response.response ?: emptyList()

                    // Sort fixtures by date and round
                    val sortedFixtures = fixtures.sortedWith(
                        compareBy<SimpleFixture> { it.fixture?.round }
                            .thenBy { it.fixture?.date }
                    )

                    println("⚽ DEBUG: LeagueDetailsViewModel - Total fixtures collected: ${fixtures.size}")

                    _uiState.value = _uiState.value.copy(
                        fixturesLoading = false,
                        fixtures = sortedFixtures,
                        fixturesError = if (fixtures.isEmpty()) "No fixtures available for this league" else null
                    )
                }
            },
            onError = { error ->
                viewModelScope.launch {
                    println("🔴 DEBUG: LeagueDetailsViewModel - Fixtures error: $error")
                    _uiState.value = _uiState.value.copy(
                        fixturesLoading = false,
                        fixturesError = error
                    )
                }
            }
        )
    }

    private fun loadTopScorers(leagueId: Int, season: Int) {
        println("🏆 DEBUG: LeagueDetailsViewModel - Loading top scorers for league $leagueId, season $season")
        _uiState.value = _uiState.value.copy(
            topScorersLoading = true,
            topScorersError = null
        )

        volleyService.getTopScorers(
            leagueId = leagueId,
            season = season,
            onSuccess = { response ->
                viewModelScope.launch {
                    println("🏆 DEBUG: LeagueDetailsViewModel - Processing top scorers response...")
                    val topScorers = response.response ?: emptyList()

                    println("🏆 DEBUG: LeagueDetailsViewModel - Total top scorers collected: ${topScorers.size}")

                    _uiState.value = _uiState.value.copy(
                        topScorersLoading = false,
                        topScorers = topScorers,
                        topScorersError = if (topScorers.isEmpty()) "No top scorers available for this league" else null
                    )
                }
            },
            onError = { error ->
                viewModelScope.launch {
                    println("🔴 DEBUG: LeagueDetailsViewModel - Top scorers error: $error")
                    _uiState.value = _uiState.value.copy(
                        topScorersLoading = false,
                        topScorersError = error
                    )
                }
            }
        )
    }

    fun retry() {
        val state = _uiState.value
        if (state.leagueId != null && state.season != null) {
            loadStandings(state.leagueId, state.season)
            loadFixtures(state.leagueId, state.season)
            loadTopScorers(state.leagueId, state.season)
        }
    }
}

data class LeagueDetailsUiState(
    val leagueId: Int? = null,
    val season: Int? = null,
    val leagueName: String? = null,

    // Standings
    val standingsLoading: Boolean = false,
    val standings: List<SimpleStanding> = emptyList(),
    val standingsError: String? = null,

    // Fixtures
    val fixturesLoading: Boolean = false,
    val fixtures: List<SimpleFixture> = emptyList(),
    val fixturesError: String? = null,

    // Top Scorers
    val topScorersLoading: Boolean = false,
    val topScorers: List<SimpleTopScorer> = emptyList(),
    val topScorersError: String? = null,

    // Predictions (for future implementation)
    val predictionsLoading: Boolean = false,
    val predictions: List<Any> = emptyList(), // TODO: Create Prediction model
    val predictionsError: String? = null
)