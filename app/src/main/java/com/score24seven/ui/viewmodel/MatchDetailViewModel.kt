/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.*
import com.score24seven.domain.usecase.GetMatchDetailsUseCase
import com.score24seven.domain.repository.MatchDetailRepository
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.MatchDetailAction
import com.score24seven.ui.state.MatchDetailEffect
import com.score24seven.ui.state.MatchDetailState
import com.score24seven.ui.state.MatchDetailTab
import com.score24seven.ui.state.UiState
import com.score24seven.ui.state.FixturesData
import com.score24seven.util.dataOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase,
    private val matchDetailRepository: MatchDetailRepository,
    private val favoritesRepository: com.score24seven.domain.repository.FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MatchDetailState())
    val state: StateFlow<MatchDetailState> = _state.asStateFlow()

    private val _effects = Channel<MatchDetailEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var currentMatchId: Int? = null

    fun handleAction(action: MatchDetailAction) {
        when (action) {
            is MatchDetailAction.LoadMatch -> {
                loadMatch(action.matchId)
            }

            is MatchDetailAction.LoadMatchEvents -> {
                loadMatchEvents(action.matchId)
            }

            is MatchDetailAction.LoadMatchLineups -> {
                loadMatchLineups(action.matchId)
            }

            is MatchDetailAction.LoadMatchStatistics -> {
                loadMatchStatistics(action.matchId)
            }

            is MatchDetailAction.LoadHeadToHead -> {
                loadHeadToHead(action.homeTeamId, action.awayTeamId)
            }

            is MatchDetailAction.LoadStandings -> {
                loadStandings(action.leagueId, action.season)
            }

            is MatchDetailAction.LoadMatchFixtures -> {
                // Replaced with loadTeamFixtures - this action is deprecated
            }

            is MatchDetailAction.RefreshMatch -> {
                currentMatchId?.let { loadMatch(it) }
            }

            is MatchDetailAction.RefreshAll -> {
                currentMatchId?.let { matchId ->
                    loadMatch(matchId)
                    loadMatchEvents(matchId)
                    loadMatchLineups(matchId)
                    loadMatchStatistics(matchId)
                    // Load head-to-head when we have match data
                    _state.value.match.dataOrNull()?.let { match ->
                        loadHeadToHead(match.homeTeam.id, match.awayTeam.id)
                    }
                }
            }

            is MatchDetailAction.SelectTab -> {
                updateState { copy(selectedTab = action.tab) }
                // Load data for the selected tab if not already loaded
                currentMatchId?.let { matchId ->
                    when (action.tab) {
                        MatchDetailTab.FIXTURES -> {
                            if (_state.value.fixtures is UiState.Loading) {
                                _state.value.match.dataOrNull()?.let { match ->
                                    loadTeamFixtures(match.homeTeam.id, match.awayTeam.id, match.league.season ?: 2024)
                                }
                            }
                        }
                        MatchDetailTab.LINEUPS -> {
                            if (_state.value.lineups is UiState.Loading) {
                                loadMatchLineups(matchId)
                            }
                        }
                        MatchDetailTab.STATISTICS -> {
                            if (_state.value.statistics is UiState.Loading) {
                                loadMatchStatistics(matchId)
                            }
                        }
                        MatchDetailTab.STANDINGS -> {
                            if (_state.value.standings is UiState.Loading) {
                                _state.value.match.dataOrNull()?.let { match ->
                                    loadStandings(match.league.id, match.league.season ?: 2024)
                                }
                            }
                        }
                        MatchDetailTab.HEAD_TO_HEAD -> {
                            if (_state.value.headToHead is UiState.Loading) {
                                _state.value.match.dataOrNull()?.let { match ->
                                    loadHeadToHead(match.homeTeam.id, match.awayTeam.id)
                                }
                            }
                        }
                        else -> {
                            // Overview tab doesn't need additional data loading
                        }
                    }
                }
            }

            is MatchDetailAction.ToggleLiveSubscription -> {
                toggleLiveSubscription()
            }

            is MatchDetailAction.ToggleFavorite -> {
                toggleFavorite()
            }

            is MatchDetailAction.ShareMatch -> {
                shareMatch()
            }

            is MatchDetailAction.NavigateBack -> {
                viewModelScope.launch {
                    _effects.send(MatchDetailEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadMatch(matchId: Int) {
        currentMatchId = matchId
        updateState { copy(isRefreshing = true) }

        getMatchDetailsUseCase(matchId)
            .onEach { resource ->
                val uiState = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> {
                        val match = resource.data!!
                        // Load all additional data when match data is available
                        loadStandings(match.league.id, match.league.season ?: 2024)
                        loadTeamFixtures(match.homeTeam.id, match.awayTeam.id, match.league.season ?: 2024)
                        loadMatchLineups(matchId)
                        loadMatchStatistics(matchId)
                        loadMatchEvents(matchId)
                        loadHeadToHead(match.homeTeam.id, match.awayTeam.id)
                        UiState.Success(match)
                    }
                    is Resource.Error -> UiState.Error(resource.message ?: "Unknown error")
                }
                updateState {
                    copy(
                        match = uiState,
                        isRefreshing = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMatchEvents(matchId: Int) {
        matchDetailRepository.getMatchEvents(matchId)
            .onEach { resource ->
                val uiState: UiState<List<MatchEvent>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data ?: emptyList())
                    is Resource.Error -> UiState.Error(resource.message ?: "Unknown error")
                }
                updateState { copy(events = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMatchLineups(matchId: Int) {
        matchDetailRepository.getMatchLineups(matchId)
            .onEach { resource ->
                val uiState: UiState<List<Lineup>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data ?: emptyList())
                    is Resource.Error -> UiState.Error(resource.message ?: "Unknown error")
                }
                updateState { copy(lineups = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMatchStatistics(matchId: Int) {
        matchDetailRepository.getMatchStatistics(matchId)
            .onEach { resource ->
                val uiState: UiState<List<MatchStatistic>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data ?: emptyList())
                    is Resource.Error -> UiState.Error(resource.message ?: "Unknown error")
                }
                updateState { copy(statistics = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadHeadToHead(homeTeamId: Int, awayTeamId: Int) {
        matchDetailRepository.getHeadToHead(homeTeamId, awayTeamId)
            .onEach { resource ->
                val uiState: UiState<List<Match>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data ?: emptyList())
                    is Resource.Error -> UiState.Error(resource.message ?: "Unknown error")
                }
                updateState { copy(headToHead = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadStandings(leagueId: Int, season: Int) {
        matchDetailRepository.getLeagueStandings(leagueId, season)
            .onEach { resource ->
                val uiState: UiState<List<Standing>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data ?: emptyList())
                    is Resource.Error -> UiState.Error(resource.message ?: "Unknown error")
                }
                updateState { copy(standings = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadTeamFixtures(homeTeamId: Int, awayTeamId: Int, season: Int) {
        viewModelScope.launch {
            updateState { copy(fixtures = UiState.Loading) }

            try {
                // Load fixtures for both teams
                val homeTeamFixtures = mutableListOf<Match>()
                val awayTeamFixtures = mutableListOf<Match>()

                // Get home team fixtures
                matchDetailRepository.getTeamFixtures(homeTeamId, season, 5)
                    .collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                homeTeamFixtures.addAll(resource.data ?: emptyList())
                            }
                            is Resource.Error -> {
                                // Handle home team error
                            }
                            else -> { /* Loading state */ }
                        }
                    }

                // Get away team fixtures
                matchDetailRepository.getTeamFixtures(awayTeamId, season, 5)
                    .collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                awayTeamFixtures.addAll(resource.data ?: emptyList())

                                // Update state when both are loaded
                                val fixturesData = FixturesData(
                                    homeTeamFixtures = homeTeamFixtures.take(5),
                                    awayTeamFixtures = awayTeamFixtures.take(5)
                                )
                                updateState { copy(fixtures = UiState.Success(fixturesData)) }
                            }
                            is Resource.Error -> {
                                updateState { copy(fixtures = UiState.Error(resource.message ?: "Unable to load team fixtures")) }
                            }
                            else -> { /* Loading state */ }
                        }
                    }
            } catch (e: Exception) {
                updateState { copy(fixtures = UiState.Error("Failed to load team fixtures")) }
            }
        }
    }

    private fun toggleLiveSubscription() {
        viewModelScope.launch {
            try {
                val matchId = currentMatchId ?: return@launch
                val currentState = _state.value

                if (currentState.isSubscribedToLive) {
                    getMatchDetailsUseCase.unsubscribeFromLiveUpdates(matchId)
                    updateState { copy(isSubscribedToLive = false) }
                    _effects.send(MatchDetailEffect.ShowSnackbar("Unsubscribed from live updates"))
                } else {
                    getMatchDetailsUseCase.subscribeToLiveUpdates(matchId)
                    updateState { copy(isSubscribedToLive = true) }
                    _effects.send(MatchDetailEffect.ShowSnackbar("Subscribed to live updates"))
                }
            } catch (e: Exception) {
                _effects.send(MatchDetailEffect.ShowError("Failed to toggle live subscription"))
            }
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val matchId = currentMatchId ?: return@launch
                val isFavorite = favoritesRepository.isFavorite(matchId)

                if (isFavorite) {
                    favoritesRepository.removeFromFavorites(matchId)
                    _effects.send(MatchDetailEffect.ShowSnackbar("Match removed from favorites"))
                    println("💔 DEBUG: MatchDetailViewModel - Match $matchId removed from favorites")
                } else {
                    favoritesRepository.addToFavorites(matchId)
                    _effects.send(MatchDetailEffect.ShowSnackbar("Match added to favorites"))
                    println("💖 DEBUG: MatchDetailViewModel - Match $matchId added to favorites")
                }

                // Reload match to update isFavorite flag
                loadMatch(matchId)
            } catch (e: Exception) {
                _effects.send(MatchDetailEffect.ShowError("Failed to toggle favorite"))
                println("❌ DEBUG: MatchDetailViewModel - Failed to toggle favorite: ${e.message}")
            }
        }
    }

    private fun shareMatch() {
        viewModelScope.launch {
            try {
                val match = _state.value.match.dataOrNull()
                if (match != null) {
                    val shareText = "${match.homeTeam.name} vs ${match.awayTeam.name} - ${match.getScoreDisplay()}"
                    _effects.send(MatchDetailEffect.ShareMatch(shareText))
                } else {
                    _effects.send(MatchDetailEffect.ShowError("No match data to share"))
                }
            } catch (e: Exception) {
                _effects.send(MatchDetailEffect.ShowError("Failed to share match"))
            }
        }
    }

    private fun updateState(update: MatchDetailState.() -> MatchDetailState) {
        _state.value = _state.value.update()
    }

    override fun onCleared() {
        super.onCleared()
        // Unsubscribe from live updates when ViewModel is cleared
        viewModelScope.launch {
            currentMatchId?.let { matchId ->
                try {
                    getMatchDetailsUseCase.unsubscribeFromLiveUpdates(matchId)
                } catch (e: Exception) {
                    // Ignore errors on cleanup
                }
            }
        }
    }
}