/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.usecase.*
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveMatchDetailViewModel @Inject constructor(
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase,
    private val getMatchEventsUseCase: GetMatchEventsUseCase,
    private val getMatchLineupsUseCase: GetMatchLineupsUseCase,
    private val getMatchStatisticsUseCase: GetMatchStatisticsUseCase,
    private val getHeadToHeadUseCase: GetHeadToHeadUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LiveMatchDetailState())
    val state: StateFlow<LiveMatchDetailState> = _state.asStateFlow()

    private val _effects = Channel<MatchDetailEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var currentMatchId: Int? = null

    fun handleAction(action: LiveMatchDetailAction) {
        when (action) {
            is LiveMatchDetailAction.LoadMatch -> {
                loadMatch(action.matchId)
            }
            is LiveMatchDetailAction.RefreshMatch -> {
                currentMatchId?.let { loadMatch(it) }
            }
            is LiveMatchDetailAction.SelectTab -> {
                updateState { copy(selectedTab = action.tab) }
                loadTabData(action.tab)
            }
            is LiveMatchDetailAction.NavigateBack -> {
                viewModelScope.launch {
                    _effects.send(MatchDetailEffect.NavigateBack)
                }
            }
            is LiveMatchDetailAction.ShareMatch -> {
                shareMatch()
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
                    is Resource.Success -> UiState.Success(resource.data!!)
                    is Resource.Error -> {
                        if (resource.data != null) {
                            UiState.Success(resource.data)
                        } else {
                            UiState.Error(resource.message ?: "Unknown error")
                        }
                    }
                }
                updateState {
                    copy(
                        match = uiState,
                        isRefreshing = false
                    )
                }

                // Load initial tab data
                if (uiState is UiState.Success) {
                    loadTabData(state.value.selectedTab)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadTabData(tab: MatchDetailTab) {
        val matchId = currentMatchId ?: return

        when (tab) {
            MatchDetailTab.EVENTS -> {
                loadMatchEvents(matchId)
            }
            MatchDetailTab.LINEUPS -> {
                loadMatchLineups(matchId)
            }
            MatchDetailTab.STATISTICS -> {
                loadMatchStatistics(matchId)
            }
            MatchDetailTab.HEAD_TO_HEAD -> {
                loadHeadToHead(matchId)
            }
            else -> {
                // Overview doesn't need additional data
            }
        }
    }

    private fun loadMatchEvents(matchId: Int) {
        getMatchEventsUseCase(matchId)
            .onEach { resource ->
                val uiState = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data!!)
                    is Resource.Error -> UiState.Error(resource.message ?: "Failed to load events")
                }
                updateState { copy(events = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMatchLineups(matchId: Int) {
        getMatchLineupsUseCase(matchId)
            .onEach { resource ->
                val uiState = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data!!)
                    is Resource.Error -> UiState.Error(resource.message ?: "Failed to load lineups")
                }
                updateState { copy(lineups = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMatchStatistics(matchId: Int) {
        getMatchStatisticsUseCase(matchId)
            .onEach { resource ->
                val uiState = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data!!)
                    is Resource.Error -> UiState.Error(resource.message ?: "Failed to load statistics")
                }
                updateState { copy(statistics = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadHeadToHead(matchId: Int) {
        getHeadToHeadUseCase(matchId)
            .onEach { resource ->
                val uiState = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data!!)
                    is Resource.Error -> UiState.Error(resource.message ?: "Failed to load head-to-head")
                }
                updateState { copy(headToHead = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun shareMatch() {
        viewModelScope.launch {
            try {
                val match = _state.value.match.dataOrNull()
                if (match != null) {
                    val shareText = "${match.homeTeam.name} vs ${match.awayTeam.name}"
                    _effects.send(MatchDetailEffect.ShareMatch(shareText))
                } else {
                    _effects.send(MatchDetailEffect.ShowError("No match data to share"))
                }
            } catch (e: Exception) {
                _effects.send(MatchDetailEffect.ShowError("Failed to share match"))
            }
        }
    }

    private fun updateState(update: LiveMatchDetailState.() -> LiveMatchDetailState) {
        _state.value = _state.value.update()
    }
}

data class LiveMatchDetailState(
    val match: UiState<com.score24seven.domain.model.Match> = UiState.Loading,
    val events: UiState<List<MatchEvent>> = UiState.Loading,
    val lineups: UiState<List<TeamLineup>> = UiState.Loading,
    val statistics: UiState<List<TeamStatistics>> = UiState.Loading,
    val headToHead: UiState<HeadToHeadData> = UiState.Loading,
    val selectedTab: MatchDetailTab = MatchDetailTab.OVERVIEW,
    val isRefreshing: Boolean = false
)

sealed class LiveMatchDetailAction {
    data class LoadMatch(val matchId: Int) : LiveMatchDetailAction()
    object RefreshMatch : LiveMatchDetailAction()
    data class SelectTab(val tab: MatchDetailTab) : LiveMatchDetailAction()
    object NavigateBack : LiveMatchDetailAction()
    object ShareMatch : LiveMatchDetailAction()
}