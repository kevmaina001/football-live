/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.state

import com.score24seven.domain.model.League
import com.score24seven.domain.model.Match

data class HomeScreenState(
    val liveMatches: UiState<List<Match>> = UiState.Loading,
    val todayMatches: UiState<List<Match>> = UiState.Loading,
    val favoriteMatches: UiState<List<Match>> = UiState.Loading,
    val featuredLeagues: UiState<List<League>> = UiState.Loading,
    val selectedLeague: League? = null,
    val isRefreshing: Boolean = false
)

sealed class HomeScreenAction {
    object RefreshData : HomeScreenAction()
    object LoadLiveMatches : HomeScreenAction()
    object LoadTodayMatches : HomeScreenAction()
    object LoadFeaturedLeagues : HomeScreenAction()
    data class SelectLeague(val league: League?) : HomeScreenAction()
    data class NavigateToMatch(val matchId: Int) : HomeScreenAction()
    data class ToggleMatchFavorite(val matchId: Int) : HomeScreenAction()
    data class SubscribeToLiveMatch(val matchId: Int) : HomeScreenAction()
    data class UnsubscribeFromLiveMatch(val matchId: Int) : HomeScreenAction()
}

sealed class HomeScreenEffect {
    data class NavigateToMatchDetail(val matchId: Int) : HomeScreenEffect()
    data class ShowError(val message: String) : HomeScreenEffect()
    data class ShowSnackbar(val message: String) : HomeScreenEffect()
}