/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.state

import com.score24seven.domain.model.*

data class TeamDetailState(
    val team: UiState<Team> = UiState.Loading,
    val statistics: UiState<TeamStatistics> = UiState.Loading,
    val fixtures: UiState<List<Match>> = UiState.Loading,
    val players: UiState<List<Player>> = UiState.Loading,
    val isFavorite: Boolean = false,
    val isRefreshing: Boolean = false
)

sealed class TeamDetailAction {
    data class LoadTeamDetails(val teamId: Int) : TeamDetailAction()
    data class LoadTeamStatistics(val teamId: Int, val leagueId: Int, val season: Int) : TeamDetailAction()
    data class LoadTeamFixtures(val teamId: Int, val season: Int) : TeamDetailAction()
    data class LoadTeamPlayers(val teamId: Int, val season: Int) : TeamDetailAction()
    object ToggleFavorite : TeamDetailAction()
    object ShareTeam : TeamDetailAction()
    object RefreshAll : TeamDetailAction()
}

sealed class TeamDetailEffect {
    data class ShareTeam(val text: String) : TeamDetailEffect()
    data class ShowError(val message: String) : TeamDetailEffect()
    data class ShowSnackbar(val message: String) : TeamDetailEffect()
}