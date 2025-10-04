/*
 * MatchesViewModel for Score24Seven
 * Handles match data loading by date
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.domain.model.LeagueInfo
import com.score24seven.domain.model.Match
import com.score24seven.domain.repository.LeagueInfoRepository
import com.score24seven.domain.repository.MatchRepository
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.UiState
import com.score24seven.util.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    private val leagueInfoRepository: LeagueInfoRepository,
    private val favoritesRepository: com.score24seven.domain.repository.FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MatchesState())
    val state: StateFlow<MatchesState> = _state.asStateFlow()

    init {
        loadFavoriteLeagues()
        loadMatchesForDate(LocalDate.now())
    }

    fun loadMatchesForDate(date: LocalDate) {
        println("DEBUG: MatchesViewModel - loadMatchesForDate called with date: $date")
        viewModelScope.launch {
            _state.update { it.copy(matches = UiState.Loading, selectedDate = date) }
            println("DEBUG: MatchesViewModel - State updated with loading for date: $date")

            matchRepository.getMatchesForDate(date.toString()).collect { resource ->
                println("DEBUG: MatchesViewModel - Received resource: ${resource::class.simpleName} for date: $date")
                val uiState: UiState<List<Match>> = when (resource) {
                    is Resource.Loading -> {
                        println("DEBUG: MatchesViewModel - Resource is Loading")
                        UiState.Loading
                    }
                    is Resource.Success -> {
                        val sortedMatches = resource.data?.sortedBy { it.fixture.timestamp } ?: emptyList()
                        println("DEBUG: MatchesViewModel - Resource Success with ${sortedMatches.size} matches for date: $date")
                        sortedMatches.forEach { match ->
                            println("DEBUG: MatchesViewModel - Match: ${match.homeTeam.name} vs ${match.awayTeam.name} on ${match.fixture.dateTime}")
                        }
                        UiState.Success(sortedMatches)
                    }
                    is Resource.Error -> {
                        println("DEBUG: MatchesViewModel - Resource Error: ${resource.message}")
                        UiState.Error(resource.message ?: "Unknown error")
                    }
                }

                _state.update { currentState -> currentState.copy(matches = uiState) }
                updateMatchCounts()
                println("DEBUG: MatchesViewModel - Final state updated for date: $date")
            }
        }
    }

    fun refreshMatches(date: LocalDate) {
        loadMatchesForDate(date)
    }

    private fun loadFavoriteLeagues() {
        println("🟢 DEBUG: MatchesViewModel - loadFavoriteLeagues() called - showing all favorites")
        viewModelScope.launch {
            _state.update { it.copy(favoriteLeagues = UiState.Loading) }

            // Always show favorite competitions, get match data to supplement
            loadAllFavoriteCompetitions()
        }
    }

    private fun loadAllFavoriteCompetitions() {
        println("🟢 DEBUG: MatchesViewModel - loadAllFavoriteCompetitions() called - optimized loading")
        viewModelScope.launch {
            try {
                // OPTIMIZED: Create favorites instantly from config while loading matches
                val instantFavorites = Config.PRIORITY_LEAGUE_IDS.map { leagueId ->
                    val leagueInfo = Config.FAVORITE_LEAGUE_INFO[leagueId]
                    val logoUrl = getLeagueLogoUrl(leagueId)
                    val flagUrl = getCountryFlagUrl(leagueInfo?.second)

                    println("🚀 DEBUG: Creating instant favorite - ID: $leagueId, Name: ${leagueInfo?.first}, Logo: $logoUrl")

                    LeagueInfo(
                        league = com.score24seven.domain.model.LeagueDetails(
                            id = leagueId,
                            name = leagueInfo?.first ?: "League $leagueId",
                            type = "League",
                            logo = logoUrl // Add proper logo URLs
                        ),
                        country = com.score24seven.domain.model.CountryDetails(
                            name = leagueInfo?.second ?: "Unknown",
                            code = null,
                            flag = flagUrl // Add flag URLs
                        ),
                        seasons = emptyList()
                    )
                }

                // Show favorites immediately for better UX
                _state.update {
                    it.copy(favoriteLeagues = UiState.Success(instantFavorites))
                }

                // Get matches to supplement with real data (async)
                matchRepository.getMatchesForDate(LocalDate.now().toString()).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val matches = resource.data ?: emptyList()
                            println("🟢 DEBUG: MatchesViewModel - Got ${matches.size} matches to supplement favorites")

                            // Create all league objects from matches
                            val allLeaguesFromMatches = mutableMapOf<Int, LeagueInfo>()
                            matches.forEach { match ->
                                if (!allLeaguesFromMatches.containsKey(match.league.id)) {
                                    allLeaguesFromMatches[match.league.id] = LeagueInfo(
                                        league = com.score24seven.domain.model.LeagueDetails(
                                            id = match.league.id,
                                            name = match.league.name,
                                            type = "League",
                                            logo = match.league.logo
                                        ),
                                        country = com.score24seven.domain.model.CountryDetails(
                                            name = match.league.country ?: "Unknown",
                                            code = null,
                                            flag = null
                                        ),
                                        seasons = emptyList()
                                    )
                                }
                            }

                            // OPTIMIZED: Update favorites with real match data if available
                            val updatedFavorites = Config.PRIORITY_LEAGUE_IDS.map { leagueId ->
                                // Use actual match data if available, otherwise keep instant favorites
                                allLeaguesFromMatches[leagueId] ?: instantFavorites.find { it.league.id == leagueId }!!
                            }

                            println("🟢 DEBUG: MatchesViewModel - Updated ${updatedFavorites.size} favorite competitions with match data")

                            // Update state with updated favorites and all leagues
                            _state.update {
                                it.copy(
                                    favoriteLeagues = UiState.Success(updatedFavorites),
                                    allLeagues = UiState.Success(allLeaguesFromMatches.values.toList())
                                )
                            }
                        }
                        is Resource.Error -> {
                            println("🔴 DEBUG: MatchesViewModel - Error getting matches: ${resource.message}")
                            _state.update { it.copy(favoriteLeagues = UiState.Error("Failed to get matches: ${resource.message}")) }
                        }
                        is Resource.Loading -> {
                            println("🟢 DEBUG: MatchesViewModel - Loading matches")
                        }
                    }
                }
            } catch (e: Exception) {
                println("🔴 DEBUG: MatchesViewModel - Exception loading favorite competitions: ${e.message}")
                _state.update { it.copy(favoriteLeagues = UiState.Error("Exception loading favorites: ${e.message}")) }
            }
        }
    }


    fun selectLeague(leagueId: Int?) {
        _state.update { it.copy(selectedLeagueId = leagueId) }
    }

    fun loadMatchesForLeague(leagueId: Int, date: LocalDate) {
        viewModelScope.launch {
            _state.update { it.copy(matches = UiState.Loading, selectedDate = date, selectedLeagueId = leagueId) }

            matchRepository.getLeagueMatches(leagueId, date.year).collect { resource ->
                val uiState: UiState<List<Match>> = when (resource) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> {
                        val matchesForDate = resource.data?.filter { match ->
                            match.fixture.dateTime.toLocalDate() == date
                        }?.sortedBy { it.fixture.timestamp } ?: emptyList()
                        UiState.Success(matchesForDate)
                    }
                    is Resource.Error -> UiState.Error(resource.message ?: "Unknown error")
                }
                _state.update { it.copy(matches = uiState) }
                updateMatchCounts()
            }
        }
    }

    fun getAvailableDates(): List<LocalDate> {
        val today = LocalDate.now()
        return listOf(
            today.minusDays(3),
            today.minusDays(2),
            today.minusDays(1),
            today,              // Today
            today.plusDays(1),
            today.plusDays(2),
            today.plusDays(3)
        )
    }

    fun getDateDisplayName(date: LocalDate): String {
        val today = LocalDate.now()
        return when (date) {
            today -> "TODAY"
            else -> {
                val dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEE")).uppercase()
                val dayMonth = date.format(DateTimeFormatter.ofPattern("dd.MM"))
                "$dayOfWeek\n$dayMonth"
            }
        }
    }

    fun getShortDateDisplay(date: LocalDate): String {
        val today = LocalDate.now()
        return when (date) {
            today -> "TODAY"
            else -> date.format(DateTimeFormatter.ofPattern("EEE\ndd.MM")).uppercase()
        }
    }


    fun updateMatchCounts() {
        val currentMatches = (_state.value.matches as? UiState.Success)?.data ?: emptyList()
        val totalCount = currentMatches.size
        val liveCount = currentMatches.count { it.status.short == "LIVE" || it.status.short == "1H" || it.status.short == "2H" || it.status.short == "HT" }

        _state.update {
            it.copy(
                totalMatchCount = totalCount,
                liveMatchCount = liveCount
            )
        }
    }

    fun toggleCountryExpansion(countryName: String) {
        _state.update { currentState ->
            val expandedCountries = currentState.expandedCountries.toMutableSet()
            if (expandedCountries.contains(countryName)) {
                expandedCountries.remove(countryName)
            } else {
                expandedCountries.add(countryName)
            }
            currentState.copy(expandedCountries = expandedCountries)
        }
    }

    // LOGO FIX: Helper functions for league logos and country flags - matches Config.PRIORITY_LEAGUE_IDS
    private fun getLeagueLogoUrl(leagueId: Int): String? {
        println("🏆 DEBUG: Getting league logo URL for league ID: $leagueId")
        val logoUrl = when (leagueId) {
            // Major leagues from Config.PRIORITY_LEAGUE_IDS
            39 -> "https://media.api-sports.io/football/leagues/39.png" // Premier League (England)
            2 -> "https://media.api-sports.io/football/leagues/2.png" // UEFA Champions League
            3 -> "https://media.api-sports.io/football/leagues/3.png" // UEFA Europa League
            1 -> "https://media.api-sports.io/football/leagues/1.png" // World Cup (FIFA)
            140 -> "https://media.api-sports.io/football/leagues/140.png" // La Liga (Spain)
            78 -> "https://media.api-sports.io/football/leagues/78.png" // Bundesliga (Germany)
            135 -> "https://media.api-sports.io/football/leagues/135.png" // Serie A (Italy)
            61 -> "https://media.api-sports.io/football/leagues/61.png" // Ligue 1 (France)
            5 -> "https://media.api-sports.io/football/leagues/5.png" // UEFA Nations League
            848 -> "https://media.api-sports.io/football/leagues/848.png" // UEFA Europa Conference League
            45 -> "https://media.api-sports.io/football/leagues/45.png" // FA Cup (England)
            40 -> "https://media.api-sports.io/football/leagues/40.png" // Championship (England)
            4 -> "https://media.api-sports.io/football/leagues/4.png" // European Championship
            253 -> "https://media.api-sports.io/football/leagues/253.png" // Major League Soccer (USA)
            else -> {
                println("🔴 DEBUG: No logo URL mapping for league ID: $leagueId")
                null
            }
        }
        println("🏆 DEBUG: League ID $leagueId -> Logo URL: $logoUrl")
        return logoUrl
    }

    private fun getCountryFlagUrl(country: String?): String? {
        return when (country?.lowercase()) {
            "england" -> "https://flagcdn.com/w40/gb-eng.png"
            "spain" -> "https://flagcdn.com/w40/es.png"
            "germany" -> "https://flagcdn.com/w40/de.png"
            "italy" -> "https://flagcdn.com/w40/it.png"
            "france" -> "https://flagcdn.com/w40/fr.png"
            "europe" -> "https://flagcdn.com/w40/eu.png"
            "usa" -> "https://flagcdn.com/w40/us.png"
            else -> null
        }
    }

    fun toggleFavorite(matchId: Int) {
        viewModelScope.launch {
            try {
                val isFavorite = favoritesRepository.isFavorite(matchId)
                if (isFavorite) {
                    favoritesRepository.removeFromFavorites(matchId)
                    println("💔 DEBUG: MatchesViewModel - Match $matchId removed from favorites")
                } else {
                    favoritesRepository.addToFavorites(matchId)
                    println("💖 DEBUG: MatchesViewModel - Match $matchId added to favorites")
                }
            } catch (e: Exception) {
                println("❌ DEBUG: MatchesViewModel - Failed to toggle favorite: ${e.message}")
            }
        }
    }
}

data class MatchesState(
    val matches: UiState<List<Match>> = UiState.Loading,
    val favoriteLeagues: UiState<List<LeagueInfo>> = UiState.Loading,
    val allLeagues: UiState<List<LeagueInfo>> = UiState.Loading,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedLeagueId: Int? = null, // null means all leagues
    val isRefreshing: Boolean = false,
    val totalMatchCount: Int = 0,
    val liveMatchCount: Int = 0,
    val expandedCountries: Set<String> = emptySet() // Track which countries are expanded
)

data class LeagueWithMatches(
    val league: LeagueInfo,
    val matchCount: Int,
    val liveCount: Int
)