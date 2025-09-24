/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.data.api.FootballApiService
import com.score24seven.data.mapper.toTeamStatistics
import com.score24seven.domain.model.*
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val apiService: FootballApiService
) : ViewModel() {

    private val _state = MutableStateFlow(TeamDetailState())
    val state: StateFlow<TeamDetailState> = _state.asStateFlow()

    private val _effects = Channel<TeamDetailEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var currentTeamId: Int? = null

    fun loadTeamDetails(teamId: Int) {
        currentTeamId = teamId
        updateState { copy(isRefreshing = true) }

        // Load team basic info first
        loadTeamBasicInfo(teamId)

        // Load additional data - using default league and season for now
        // In a real app, you might want to get the team's current league
        loadTeamStatistics(teamId, 39, 2024) // Premier League as default
        loadTeamFixtures(teamId, 2024)
        loadTeamPlayers(teamId, 2024)
    }

    private fun loadTeamBasicInfo(teamId: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(team = UiState.Loading) }

                val response = apiService.getTeams(search = null)
                if (response.isSuccessful) {
                    val teamDto = response.body()?.response?.find { it.id == teamId }
                    if (teamDto != null) {
                        val team = Team(
                            id = teamDto.id,
                            name = teamDto.name,
                            code = teamDto.code,
                            logo = teamDto.logo,
                            country = teamDto.country,
                            founded = teamDto.founded,
                            isNational = teamDto.national,
                            isFavorite = false // TODO: Get from local storage
                        )
                        updateState {
                            copy(
                                team = UiState.Success(team),
                                isFavorite = team.isFavorite,
                                isRefreshing = false
                            )
                        }
                    } else {
                        updateState {
                            copy(
                                team = UiState.Error("Team not found"),
                                isRefreshing = false
                            )
                        }
                    }
                } else {
                    updateState {
                        copy(
                            team = UiState.Error("Failed to load team details"),
                            isRefreshing = false
                        )
                    }
                }
            } catch (e: Exception) {
                updateState {
                    copy(
                        team = UiState.Error(e.message ?: "Unknown error"),
                        isRefreshing = false
                    )
                }
            }
        }
    }

    private fun loadTeamStatistics(teamId: Int, leagueId: Int, season: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(statistics = UiState.Loading) }

                val response = apiService.getTeamStatistics(
                    teamId = teamId,
                    leagueId = leagueId,
                    season = season
                )

                if (response.isSuccessful) {
                    val statisticsDto = response.body()?.response
                    if (statisticsDto != null) {
                        val statistics = statisticsDto.toTeamStatistics()
                        updateState { copy(statistics = UiState.Success(statistics)) }
                    } else {
                        updateState { copy(statistics = UiState.Error("No statistics available")) }
                    }
                } else {
                    updateState { copy(statistics = UiState.Error("Failed to load statistics")) }
                }
            } catch (e: Exception) {
                updateState { copy(statistics = UiState.Error(e.message ?: "Unknown error")) }
            }
        }
    }

    private fun loadTeamFixtures(teamId: Int, season: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(fixtures = UiState.Loading) }

                val response = apiService.getTeamFixtures(
                    teamId = teamId,
                    season = season,
                    last = 10
                )

                if (response.isSuccessful) {
                    val fixtures = response.body()?.response?.map { matchDto ->
                        // Map MatchDto to Match domain model
                        // This is a simplified mapping - you might want to use existing mappers
                        Match(
                            id = matchDto.fixture.id,
                            homeTeam = Team(
                                id = matchDto.teams.home.id,
                                name = matchDto.teams.home.name,
                                logo = matchDto.teams.home.logo
                            ),
                            awayTeam = Team(
                                id = matchDto.teams.away.id,
                                name = matchDto.teams.away.name,
                                logo = matchDto.teams.away.logo
                            ),
                            league = League(
                                id = matchDto.league.id,
                                name = matchDto.league.name,
                                country = matchDto.league.country,
                                logo = matchDto.league.logo,
                                flag = matchDto.league.flag,
                                season = matchDto.league.season,
                                round = matchDto.league.round
                            ),
                            fixture = Fixture(
                                dateTime = java.time.LocalDateTime.now(), // TODO: Parse actual date
                                timezone = matchDto.fixture.timezone,
                                timestamp = matchDto.fixture.timestamp
                            ),
                            score = Score(
                                home = matchDto.goals.home,
                                away = matchDto.goals.away,
                                halftime = matchDto.score.halftime?.let {
                                    ScorePeriod(it.home, it.away)
                                },
                                fulltime = matchDto.score.fulltime?.let {
                                    ScorePeriod(it.home, it.away)
                                }
                            ),
                            status = MatchStatus(
                                short = matchDto.fixture.status.short,
                                long = matchDto.fixture.status.long,
                                elapsed = matchDto.fixture.status.elapsed
                            )
                        )
                    } ?: emptyList()

                    updateState { copy(fixtures = UiState.Success(fixtures)) }
                } else {
                    updateState { copy(fixtures = UiState.Error("Failed to load fixtures")) }
                }
            } catch (e: Exception) {
                updateState { copy(fixtures = UiState.Error(e.message ?: "Unknown error")) }
            }
        }
    }

    private fun loadTeamPlayers(teamId: Int, season: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(players = UiState.Loading) }

                val response = apiService.getPlayers(
                    teamId = teamId,
                    season = season,
                    page = 1
                )

                if (response.isSuccessful) {
                    val players = response.body()?.response?.map { playerDto ->
                        Player(
                            id = playerDto.id,
                            name = playerDto.name,
                            firstName = playerDto.firstname,
                            lastName = playerDto.lastname,
                            age = playerDto.age,
                            nationality = playerDto.nationality,
                            position = playerDto.statistics?.firstOrNull()?.games?.position,
                            number = playerDto.statistics?.firstOrNull()?.games?.number,
                            photo = playerDto.photo,
                            isInjured = playerDto.injured ?: false
                        )
                    } ?: emptyList()

                    updateState { copy(players = UiState.Success(players)) }
                } else {
                    updateState { copy(players = UiState.Error("Failed to load players")) }
                }
            } catch (e: Exception) {
                updateState { copy(players = UiState.Error(e.message ?: "Unknown error")) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val currentFavorite = _state.value.isFavorite
                updateState { copy(isFavorite = !currentFavorite) }

                // TODO: Update favorite status in local database

                val message = if (!currentFavorite) "Added to favorites" else "Removed from favorites"
                _effects.send(TeamDetailEffect.ShowSnackbar(message))
            } catch (e: Exception) {
                _effects.send(TeamDetailEffect.ShowError("Failed to update favorite status"))
            }
        }
    }

    fun shareTeam() {
        viewModelScope.launch {
            try {
                val team = _state.value.team
                if (team is UiState.Success) {
                    val shareText = "Check out ${team.data.name} team details!"
                    _effects.send(TeamDetailEffect.ShareTeam(shareText))
                } else {
                    _effects.send(TeamDetailEffect.ShowError("No team data to share"))
                }
            } catch (e: Exception) {
                _effects.send(TeamDetailEffect.ShowError("Failed to share team"))
            }
        }
    }

    private fun updateState(update: TeamDetailState.() -> TeamDetailState) {
        _state.value = _state.value.update()
    }
}