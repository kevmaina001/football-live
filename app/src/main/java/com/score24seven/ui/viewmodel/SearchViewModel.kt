/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.Team
import com.score24seven.domain.model.League
import com.score24seven.domain.repository.MatchRepository
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

        try {
            // Use the repository's search function
            var searchError: String? = null
            val allMatches = mutableListOf<Match>()

            // Collect search results
            matchRepository.searchMatches(query).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        allMatches.addAll(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        searchError = resource.message
                    }
                    else -> {}
                }
            }

            // If there was an error, return it
            if (searchError != null) {
                return SearchResults(error = searchError)
            }

            // Filter matches by query (additional client-side filtering for better results)
            val filteredMatches = allMatches
                .distinctBy { it.id }
                .filter { match ->
                    match.homeTeam.name.lowercase().contains(lowerQuery) ||
                    match.awayTeam.name.lowercase().contains(lowerQuery) ||
                    match.league.name.lowercase().contains(lowerQuery)
                }

            // Extract unique teams from filtered matches
            val teams = mutableListOf<Team>()
            filteredMatches.forEach { match ->
                if (match.homeTeam.name.lowercase().contains(lowerQuery)) {
                    teams.add(match.homeTeam)
                }
                if (match.awayTeam.name.lowercase().contains(lowerQuery)) {
                    teams.add(match.awayTeam)
                }
            }

            // Extract unique leagues from all matches
            val leagues = allMatches
                .map { it.league }
                .distinctBy { it.id }
                .filter { league ->
                    league.name.lowercase().contains(lowerQuery) ||
                    league.country.lowercase().contains(lowerQuery)
                }

            return SearchResults(
                matches = filteredMatches,
                teams = teams.distinctBy { it.id },
                leagues = leagues
            )
        } catch (e: Exception) {
            return SearchResults(error = e.message)
        }
    }
}

data class SearchResults(
    val matches: List<Match> = emptyList(),
    val teams: List<Team> = emptyList(),
    val leagues: List<League> = emptyList(),
    val error: String? = null
)
