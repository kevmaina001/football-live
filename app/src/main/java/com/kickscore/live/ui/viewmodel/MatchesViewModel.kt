/*
 * MatchesViewModel for KickScore Live
 * Handles match data loading by date
 */

package com.kickscore.live.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kickscore.live.domain.model.LeagueInfo
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.repository.LeagueInfoRepository
import com.kickscore.live.domain.repository.MatchRepository
import com.kickscore.live.domain.util.Resource
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.util.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    private val leagueInfoRepository: LeagueInfoRepository
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
        println("🟢 DEBUG: MatchesViewModel - loadAllFavoriteCompetitions() called - showing all favorites with match counts")
        viewModelScope.launch {
            try {
                // Get matches first to supplement favorite competitions with real data
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
                                        league = com.kickscore.live.domain.model.LeagueDetails(
                                            id = match.league.id,
                                            name = match.league.name,
                                            type = "League",
                                            logo = match.league.logo
                                        ),
                                        country = com.kickscore.live.domain.model.CountryDetails(
                                            name = match.league.country ?: "Unknown",
                                            code = null,
                                            flag = null
                                        ),
                                        seasons = emptyList()
                                    )
                                }
                            }

                            // Log all leagues with their country flags for debugging
                            allLeaguesFromMatches.values.forEach { league ->
                                println("🏳️ DEBUG: League: ${league.league.name} | Country: ${league.country.name} | Flag URL: ${league.country.flag}")
                            }

                            // Create ALL favorite leagues - always show them regardless of matches
                            val favoriteLeagues = Config.PRIORITY_LEAGUE_IDS.map { leagueId ->
                                // Use actual match data if available, otherwise use config data
                                allLeaguesFromMatches[leagueId] ?: run {
                                    val leagueInfo = Config.FAVORITE_LEAGUE_INFO[leagueId]
                                    LeagueInfo(
                                        league = com.kickscore.live.domain.model.LeagueDetails(
                                            id = leagueId,
                                            name = leagueInfo?.first ?: "League $leagueId",
                                            type = "League",
                                            logo = null
                                        ),
                                        country = com.kickscore.live.domain.model.CountryDetails(
                                            name = leagueInfo?.second ?: "Unknown",
                                            code = null,
                                            flag = null
                                        ),
                                        seasons = emptyList()
                                    )
                                }
                            }

                            println("🟢 DEBUG: MatchesViewModel - Created ${favoriteLeagues.size} favorite competitions (all shown)")
                            favoriteLeagues.forEach { league ->
                                println("🟢 DEBUG: MatchesViewModel - Favorite Competition: ${league.league.name} (ID: ${league.league.id})")
                                println("🟢 DEBUG: MatchesViewModel - Logo URL: '${league.league.logo}'")
                                println("🟢 DEBUG: MatchesViewModel - Country: ${league.country.name}, Flag URL: '${league.country.flag}'")

                                // Check if this league has real match data or is from config
                                val hasRealData = allLeaguesFromMatches.containsKey(league.league.id)
                                println("🟢 DEBUG: MatchesViewModel - Has real match data: $hasRealData")
                            }

                            // Also debug specific missing competitions
                            val missingCompetitions = listOf(39, 2, 3, 253, 45) // PL, UCL, UEL, MLS, FA Cup
                            missingCompetitions.forEach { leagueId ->
                                val leagueName = Config.FAVORITE_LEAGUE_INFO[leagueId]?.first ?: "Unknown"
                                val hasMatches = allLeaguesFromMatches.containsKey(leagueId)
                                println("🔍 DEBUG: Competition $leagueName (ID: $leagueId) - Has matches: $hasMatches")
                                if (hasMatches) {
                                    val league = allLeaguesFromMatches[leagueId]
                                    println("🔍 DEBUG: $leagueName logo: '${league?.league?.logo}'")
                                }
                            }

                            // Update state with all favorites and all leagues
                            _state.update {
                                it.copy(
                                    favoriteLeagues = UiState.Success(favoriteLeagues),
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