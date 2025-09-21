/*
 * Leagues ViewModel for KickScore Live
 * Handles league selection, standings, top scorers, and teams with real API data
 */

package com.kickscore.live.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Standing
import com.kickscore.live.domain.model.Team
import com.kickscore.live.domain.model.TopScorer
import com.kickscore.live.domain.repository.StandingRepository
import com.kickscore.live.domain.repository.TeamRepository
import com.kickscore.live.domain.repository.TopScorerRepository
import com.kickscore.live.domain.util.Resource
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.util.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val standingRepository: StandingRepository,
    private val topScorerRepository: TopScorerRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaguesState())
    val state: StateFlow<LeaguesState> = _state.asStateFlow()

    init {
        loadFavoriteLeagues()
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
                season = 2024,
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
    }

    private fun loadLeagueData(leagueId: Int, season: Int) {
        println("🏆 DEBUG: LeaguesViewModel - Loading data for league $leagueId, season $season")
        loadStandings(leagueId, season)
        loadTopScorers(leagueId, season)
        loadTeams(leagueId, season)
    }

    private fun loadStandings(leagueId: Int, season: Int) {
        viewModelScope.launch {
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
    }

    private fun loadTopScorers(leagueId: Int, season: Int) {
        viewModelScope.launch {
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
    }

    private fun loadTeams(leagueId: Int, season: Int) {
        viewModelScope.launch {
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
    }

    fun selectTab(tab: LeagueTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun refreshData() {
        val selectedLeague = _state.value.selectedLeague
        if (selectedLeague != null) {
            val season = selectedLeague.season ?: 2024
            loadLeagueData(selectedLeague.id, season)
        }
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