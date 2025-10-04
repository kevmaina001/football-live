/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.Team
import com.score24seven.domain.repository.MatchRepository
import com.score24seven.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<SearchResults>(SearchResults())
    val searchResults: StateFlow<SearchResults> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        // Observe search query and trigger search with debounce
        searchQuery
            .debounce(300) // Wait 300ms after user stops typing
            .filter { it.isNotBlank() && it.length >= 2 }
            .onEach { _isSearching.value = true }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                flow {
                    emit(performSearch(query))
                }
            }
            .onEach {
                _searchResults.value = it
                _isSearching.value = false
            }
            .catch { e ->
                _isSearching.value = false
                // Handle error
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = SearchResults()
            _isSearching.value = false
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = SearchResults()
        _isSearching.value = false
    }

    private suspend fun performSearch(query: String): SearchResults {
        val lowerQuery = query.lowercase()

        return try {
            // Get all matches from today
            val todayMatchesResult = matchRepository.getTodayMatches()

            val matches = when (todayMatchesResult) {
                is UiState.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val matchList = todayMatchesResult.data as? List<Match> ?: emptyList()
                    matchList.filter { match ->
                        match.homeTeam.name.lowercase().contains(lowerQuery) ||
                        match.awayTeam.name.lowercase().contains(lowerQuery) ||
                        match.league.name.lowercase().contains(lowerQuery)
                    }
                }
                else -> emptyList()
            }

            // Extract unique teams from matches
            val teams = mutableListOf<Team>()
            matches.forEach { match ->
                if (match.homeTeam.name.lowercase().contains(lowerQuery)) {
                    teams.add(match.homeTeam)
                }
                if (match.awayTeam.name.lowercase().contains(lowerQuery)) {
                    teams.add(match.awayTeam)
                }
            }

            SearchResults(
                matches = matches,
                teams = teams.distinctBy { it.id }
            )
        } catch (e: Exception) {
            SearchResults(error = e.message)
        }
    }
}

data class SearchResults(
    val matches: List<Match> = emptyList(),
    val teams: List<Team> = emptyList(),
    val error: String? = null
)
