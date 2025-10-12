/*
 * Leagues ViewModel for Score24Seven
 * Handles league selection, standings, top scorers, and teams with real API data
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.League
import com.score24seven.domain.model.Standing
import com.score24seven.domain.model.Team
import com.score24seven.domain.model.TopScorer
import com.score24seven.domain.repository.StandingRepository
import com.score24seven.domain.repository.TeamRepository
import com.score24seven.domain.repository.TopScorerRepository
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.UiState
import com.score24seven.util.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val standingRepository: StandingRepository,
    private val topScorerRepository: TopScorerRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaguesState())
    val state: StateFlow<LeaguesState> = _state.asStateFlow()

    private var autoRefreshJob: Job? = null

    init {
        loadFavoriteLeagues()
    }

    override fun onCleared() {
        super.onCleared()
        stopAutoRefresh()
    }

    private fun loadFavoriteLeagues() {
        println("🏆 DEBUG: LeaguesViewModel - Loading favorite leagues")

        // Create leagues from Config with real league IDs
        val favoriteLeagues = Config.PRIORITY_LEAGUE_IDS.take(6).mapIndexed { _, leagueId ->
            val leagueInfo = Config.FAVORITE_LEAGUE_INFO[leagueId]
            League(
                id = leagueId,
                name = leagueInfo?.first ?: "League $leagueId",
                country = leagueInfo?.second ?: "Unknown",
                logo = null,
                flag = getCountryFlag(leagueInfo?.second),
                season = 2025,
                round = null,
                type = "League"
            )
        }

        println("🏆 DEBUG: LeaguesViewModel - Created ${favoriteLeagues.size} favorite leagues")
        _state.update { it.copy(leagues = UiState.Success(favoriteLeagues)) }

        // Select the first league by default (Premier League)
        if (favoriteLeagues.isNotEmpty()) {
            selectLeague(favoriteLeagues.first())
        }
    }

    fun selectLeague(league: League) {
        println("🏆 DEBUG: LeaguesViewModel - Selected league: ${league.name}")
        _state.update { it.copy(selectedLeague = league) }

        val currentSeason = league.season ?: 2024
        loadLeagueData(league.id, currentSeason)

        // Start auto-refresh for live standings updates
        startAutoRefresh(league.id, currentSeason)
    }

    private fun loadLeagueData(leagueId: Int, season: Int) {
        println("🏆 DEBUG: LeaguesViewModel - Loading data for league $leagueId, season $season")

        viewModelScope.launch {
            // Load all data in parallel for better performance
            val standingsDeferred = async { loadStandings(leagueId, season) }
            val topScorersDeferred = async { loadTopScorers(leagueId, season) }
            val teamsDeferred = async { loadTeams(leagueId, season) }

            // Wait for all to complete
            standingsDeferred.await()
            topScorersDeferred.await()
            teamsDeferred.await()
        }
    }

    private suspend fun loadStandings(leagueId: Int, season: Int) {
            _state.update { it.copy(standings = UiState.Loading) }

            standingRepository.getLeagueStandings(leagueId, season).collect { resource ->
                val uiState: UiState<List<Standing>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> {
                        println("🏆 DEBUG: LeaguesViewModel - Loaded ${resource.data?.size ?: 0} standings")
                        UiState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        println("🔴 DEBUG: LeaguesViewModel - Standings error: ${resource.message}")
                        UiState.Error(resource.message ?: "Failed to load standings")
                    }
                }
                _state.update { it.copy(standings = uiState) }
            }
        }

    private suspend fun loadTopScorers(leagueId: Int, season: Int) {
            _state.update { it.copy(topScorers = UiState.Loading) }

            topScorerRepository.getTopScorers(leagueId, season).collect { resource ->
                val uiState: UiState<List<TopScorer>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> {
                        println("🏆 DEBUG: LeaguesViewModel - Loaded ${resource.data?.size ?: 0} top scorers")
                        UiState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        println("🔴 DEBUG: LeaguesViewModel - Top scorers error: ${resource.message}")
                        UiState.Error(resource.message ?: "Failed to load top scorers")
                    }
                }
                _state.update { it.copy(topScorers = uiState) }
            }
        }

    private suspend fun loadTeams(leagueId: Int, season: Int) {
            _state.update { it.copy(teams = UiState.Loading) }

            teamRepository.getLeagueTeams(leagueId, season).collect { resource ->
                val uiState: UiState<List<Team>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> {
                        println("🏆 DEBUG: LeaguesViewModel - Loaded ${resource.data?.size ?: 0} teams")
                        UiState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        println("🔴 DEBUG: LeaguesViewModel - Teams error: ${resource.message}")
                        UiState.Error(resource.message ?: "Failed to load teams")
                    }
                }
                _state.update { it.copy(teams = uiState) }
            }
        }

    fun selectTab(tab: LeagueTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun refreshData() {
        val selectedLeague = _state.value.selectedLeague
        if (selectedLeague != null) {
            val season = selectedLeague.season ?: 2025  // Use current season
            loadLeagueData(selectedLeague.id, season)
        }
    }

    /**
     * Start auto-refresh for standings during live matches
     * Refreshes every 2 minutes to keep standings up-to-date during live games
     */
    private fun startAutoRefresh(leagueId: Int, season: Int) {
        stopAutoRefresh() // Stop any existing refresh job

        autoRefreshJob = viewModelScope.launch {
            while (true) {
                delay(120_000L) // 2 minutes
                println("🔄 DEBUG: LeaguesViewModel - Auto-refreshing standings for league $leagueId")

                // Refresh standings only (most frequently updated during live matches)
                standingRepository.refreshStandings(leagueId, season).let { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            println("✅ DEBUG: LeaguesViewModel - Auto-refresh successful: ${resource.data?.size ?: 0} standings")
                            _state.update { it.copy(standings = UiState.Success(resource.data ?: emptyList())) }
                        }
                        is Resource.Error -> {
                            println("❌ DEBUG: LeaguesViewModel - Auto-refresh failed: ${resource.message}")
                        }
                        is Resource.Loading -> {
                            // Ignore loading state during auto-refresh to avoid UI flicker
                        }
                    }
                }
            }
        }
    }

    /**
     * Stop auto-refresh when view model is cleared or league changes
     */
    private fun stopAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = null
    }

    private fun getCountryFlag(country: String?): String? {
        return when (country?.lowercase()) {
            "england" -> "🏴󠁧󠁢󠁥󠁮󠁧󠁿"
            "spain" -> "🇪🇸"
            "italy" -> "🇮🇹"
            "germany" -> "🇩🇪"
            "france" -> "🇫🇷"
            "europe" -> "🏆"
            "usa" -> "🇺🇸"
            else -> null
        }
    }
}

data class LeaguesState(
    val leagues: UiState<List<League>> = UiState.Loading,
    val selectedLeague: League? = null,
    val selectedTab: LeagueTab = LeagueTab.STANDINGS,
    val standings: UiState<List<Standing>> = UiState.Loading,
    val topScorers: UiState<List<TopScorer>> = UiState.Loading,
    val teams: UiState<List<Team>> = UiState.Loading
)

enum class LeagueTab(val title: String) {
    STANDINGS("Standings"),
    TOP_SCORERS("Top Scorers"),
    TEAMS("Teams")
}