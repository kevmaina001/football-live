/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.Match
import com.score24seven.domain.repository.FavoritesRepository
import com.score24seven.ui.screen.FavoriteFilter
import com.score24seven.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        println("💖 DEBUG: FavoritesViewModel - Initializing")
        loadFavorites()
    }

    private fun loadFavorites() {
        println("💖 DEBUG: FavoritesViewModel - Loading favorites")
        favoritesRepository.getFavoriteMatches()
            .onEach { favoriteMatches ->
                println("💖 DEBUG: FavoritesViewModel - Received ${favoriteMatches.size} favorite matches")
                _state.value = _state.value.copy(
                    favoriteMatches = UiState.Success(favoriteMatches)
                )
            }
            .launchIn(viewModelScope)
    }

    fun refreshFavorites() {
        println("💖 DEBUG: FavoritesViewModel - Refreshing favorites")
        _state.value = _state.value.copy(favoriteMatches = UiState.Loading)
        loadFavorites()
    }

    fun setFilter(filter: FavoriteFilter) {
        println("💖 DEBUG: FavoritesViewModel - Setting filter to: $filter")
        _state.value = _state.value.copy(selectedFilter = filter)
    }

    fun toggleFavorite(matchId: Int) {
        viewModelScope.launch {
            try {
                val isFavorite = favoritesRepository.isFavorite(matchId)
                if (isFavorite) {
                    favoritesRepository.removeFromFavorites(matchId)
                    println("💔 DEBUG: FavoritesViewModel - Match $matchId removed from favorites")
                } else {
                    favoritesRepository.addToFavorites(matchId)
                    println("💖 DEBUG: FavoritesViewModel - Match $matchId added to favorites")
                }
            } catch (e: Exception) {
                println("❌ DEBUG: FavoritesViewModel - Failed to toggle favorite: ${e.message}")
                _state.value = _state.value.copy(
                    favoriteMatches = UiState.Error("Failed to update favorites")
                )
            }
        }
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            try {
                favoritesRepository.clearAllFavorites()
                println("🗑️ DEBUG: FavoritesViewModel - All favorites cleared")
            } catch (e: Exception) {
                println("❌ DEBUG: FavoritesViewModel - Failed to clear favorites: ${e.message}")
            }
        }
    }
}

data class FavoritesState(
    val favoriteMatches: UiState<List<Match>> = UiState.Loading,
    val selectedFilter: FavoriteFilter = FavoriteFilter.ALL
)
