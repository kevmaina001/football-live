/*
 * Tournament ViewModel following friend's working TournamentFragment approach
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.LeagueInfo
import com.score24seven.domain.repository.LeagueInfoRepository
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentViewModel @Inject constructor(
    private val leagueInfoRepository: LeagueInfoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TournamentState())
    val state: StateFlow<TournamentState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadLeagues()
    }

    fun loadLeagues() {
        viewModelScope.launch {
            _state.update { it.copy(leagues = UiState.Loading) }

            leagueInfoRepository.getCurrentLeagues().collect { resource ->
                val uiState: UiState<List<LeagueInfo>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> {
                        println("DEBUG: TournamentViewModel - Loaded ${resource.data?.size ?: 0} leagues")
                        UiState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        println("DEBUG: TournamentViewModel - Error: ${resource.message}")
                        UiState.Error(resource.message ?: "Failed to load leagues")
                    }
                }
                _state.update { it.copy(leagues = uiState) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            loadLeagues()
        } else {
            searchLeagues(query)
        }
    }

    private fun searchLeagues(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(leagues = UiState.Loading) }

            leagueInfoRepository.searchLeagues(query).collect { resource ->
                val uiState: UiState<List<LeagueInfo>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(resource.data ?: emptyList())
                    is Resource.Error -> UiState.Error(resource.message ?: "Search failed")
                }
                _state.update { it.copy(leagues = uiState) }
            }
        }
    }

    fun refreshLeagues() {
        loadLeagues()
    }
}

data class TournamentState(
    val leagues: UiState<List<LeagueInfo>> = UiState.Loading,
    val isRefreshing: Boolean = false
)