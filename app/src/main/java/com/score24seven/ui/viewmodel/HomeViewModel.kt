/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.Match
import com.score24seven.domain.usecase.GetLiveMatchesUseCase
import com.score24seven.domain.usecase.GetTodayMatchesUseCase
import com.score24seven.domain.repository.FavoritesRepository
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.HomeScreenAction
import com.score24seven.ui.state.HomeScreenEffect
import com.score24seven.ui.state.HomeScreenState
import com.score24seven.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLiveMatchesUseCase: GetLiveMatchesUseCase,
    private val getTodayMatchesUseCase: GetTodayMatchesUseCase,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _effects = Channel<HomeScreenEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        println("🟢 DEBUG: HomeViewModel init - starting data load")
        handleAction(HomeScreenAction.LoadLiveMatches)

        // Add delay before loading today's matches to prevent rate limiting
        viewModelScope.launch {
            delay(1000) // 1 second delay
            handleAction(HomeScreenAction.LoadTodayMatches)
        }

        observeLiveMatches()
        observeFavoriteMatches()
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        // DISABLED: Auto-refresh causes visible UI flickering
        // Live updates are now handled by LiveMatchMonitorService via WebSocket
        // Manual refresh is available via pull-to-refresh
    }

    fun handleAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.RefreshData -> {
                refreshData()
            }

            is HomeScreenAction.LoadLiveMatches -> {
                println("🟢 DEBUG: HomeViewModel handling LoadLiveMatches action")
                loadLiveMatches()
            }

            is HomeScreenAction.LoadTodayMatches -> {
                println("🟢 DEBUG: HomeViewModel handling LoadTodayMatches action")
                loadTodayMatches()
            }

            is HomeScreenAction.LoadFeaturedLeagues -> {
                loadFeaturedLeagues()
            }

            is HomeScreenAction.SelectLeague -> {
                updateState { copy(selectedLeague = action.league) }
            }

            is HomeScreenAction.NavigateToMatch -> {
                viewModelScope.launch {
                    _effects.send(HomeScreenEffect.NavigateToMatchDetail(action.matchId))
                }
            }

            is HomeScreenAction.ToggleMatchFavorite -> {
                toggleMatchFavorite(action.matchId)
            }

            is HomeScreenAction.SubscribeToLiveMatch -> {
                subscribeToLiveMatch(action.matchId)
            }

            is HomeScreenAction.UnsubscribeFromLiveMatch -> {
                unsubscribeFromLiveMatch(action.matchId)
            }
        }
    }

    private fun refreshData() {
        updateState { copy(isRefreshing = true) }
        loadLiveMatches()

        // Add delay between API calls to prevent rate limiting
        viewModelScope.launch {
            delay(1500) // 1.5 second delay for refresh
            loadTodayMatches()
            updateState { copy(isRefreshing = false) }
        }
    }

    private fun loadLiveMatches() {
        println("🟢 DEBUG: HomeViewModel.loadLiveMatches() starting")
        getLiveMatchesUseCase()
            .onEach { resource ->
                println("🟢 DEBUG: LiveMatches Resource received: ${resource::class.simpleName}")
                val uiState: UiState<List<Match>> = when (resource) {
                    is Resource.Loading -> {
                        println("🟢 DEBUG: LiveMatches - Loading state")
                        UiState.Loading
                    }
                    is Resource.Success -> {
                        println("🟢 DEBUG: LiveMatches - Success with ${resource.data?.size ?: 0} matches")
                        UiState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        println("🔴 DEBUG: LiveMatches - Error: ${resource.message}")
                        UiState.Error(resource.message ?: "Unknown error")
                    }
                }
                updateState { copy(liveMatches = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeLiveMatches() {
        getLiveMatchesUseCase.getLiveFlow()
            .onEach { matches ->
                updateState {
                    copy(liveMatches = UiState.Success(matches))
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFavoriteMatches() {
        favoritesRepository.getFavoriteMatches()
            .onEach { favoriteMatches ->
                println("💖 DEBUG: HomeViewModel - Favorite matches updated: ${favoriteMatches.size} matches")
                updateState {
                    copy(favoriteMatches = UiState.Success(favoriteMatches))
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadTodayMatches() {
        println("🟢 DEBUG: HomeViewModel.loadTodayMatches() starting")
        getTodayMatchesUseCase()
            .onEach { resource ->
                println("🟢 DEBUG: TodayMatches Resource received: ${resource::class.simpleName}")
                val uiState: UiState<List<Match>> = when (resource) {
                    is Resource.Loading -> {
                        println("🟢 DEBUG: TodayMatches - Loading state")
                        UiState.Loading
                    }
                    is Resource.Success -> {
                        println("🟢 DEBUG: TodayMatches - Success with ${resource.data?.size ?: 0} matches")
                        UiState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        println("🔴 DEBUG: TodayMatches - Error: ${resource.message}")
                        UiState.Error(resource.message ?: "Unknown error")
                    }
                }
                updateState { copy(todayMatches = uiState) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadFeaturedLeagues() {
        // TODO: Implement when LeagueRepository is created
        updateState {
            copy(featuredLeagues = UiState.Success(emptyList()))
        }
    }

    private fun toggleMatchFavorite(matchId: Int) {
        viewModelScope.launch {
            try {
                val isFavorite = favoritesRepository.isFavorite(matchId)
                if (isFavorite) {
                    favoritesRepository.removeFromFavorites(matchId)
                    _effects.send(HomeScreenEffect.ShowSnackbar("Match removed from favorites"))
                    println("💔 DEBUG: HomeViewModel - Match $matchId removed from favorites")
                } else {
                    favoritesRepository.addToFavorites(matchId)
                    _effects.send(HomeScreenEffect.ShowSnackbar("Match added to favorites"))
                    println("💖 DEBUG: HomeViewModel - Match $matchId added to favorites")
                }
            } catch (e: Exception) {
                _effects.send(HomeScreenEffect.ShowError("Failed to toggle favorite"))
                println("❌ DEBUG: HomeViewModel - Failed to toggle favorite: ${e.message}")
            }
        }
    }

    private fun subscribeToLiveMatch(matchId: Int) {
        viewModelScope.launch {
            try {
                getLiveMatchesUseCase.subscribeToMatch(matchId)
            } catch (e: Exception) {
                _effects.send(HomeScreenEffect.ShowError("Failed to subscribe to live match"))
            }
        }
    }

    private fun unsubscribeFromLiveMatch(matchId: Int) {
        viewModelScope.launch {
            try {
                getLiveMatchesUseCase.unsubscribeFromMatch(matchId)
            } catch (e: Exception) {
                _effects.send(HomeScreenEffect.ShowError("Failed to unsubscribe from live match"))
            }
        }
    }

    private fun updateState(update: HomeScreenState.() -> HomeScreenState) {
        _state.value = _state.value.update()
    }
}
