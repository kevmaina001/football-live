/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kickscore.live.domain.model.getScoreDisplay
import com.kickscore.live.domain.usecase.GetMatchDetailsUseCase
import com.kickscore.live.domain.util.Resource
import com.kickscore.live.ui.state.MatchDetailAction
import com.kickscore.live.ui.state.MatchDetailEffect
import com.kickscore.live.ui.state.MatchDetailState
import com.kickscore.live.ui.state.MatchDetailTab
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.util.dataOrNull
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
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase
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

            is MatchDetailAction.RefreshMatch -> {
                currentMatchId?.let { loadMatch(it) }
            }

            is MatchDetailAction.SelectTab -> {
                updateState { copy(selectedTab = action.tab) }
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
                    is Resource.Success -> UiState.Success(resource.data!!)
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
                // TODO: Implement favorite toggle when repository is available
                _effects.send(MatchDetailEffect.ShowSnackbar("Match favorite toggled"))
            } catch (e: Exception) {
                _effects.send(MatchDetailEffect.ShowError("Failed to toggle favorite"))
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