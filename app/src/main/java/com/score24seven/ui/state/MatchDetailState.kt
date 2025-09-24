/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.state

import com.score24seven.domain.model.*

data class FixturesData(
    val homeTeamFixtures: List<Match> = emptyList(),
    val awayTeamFixtures: List<Match> = emptyList()
)

data class MatchDetailState(
    val match: UiState<Match> = UiState.Loading,
    val events: UiState<List<MatchEvent>> = UiState.Loading,
    val fixtures: UiState<FixturesData> = UiState.Loading,
    val lineups: UiState<List<Lineup>> = UiState.Loading,
    val statistics: UiState<List<MatchStatistic>> = UiState.Loading,
    val headToHead: UiState<List<Match>> = UiState.Loading,
    val standings: UiState<List<Standing>> = UiState.Loading,
    val selectedTab: MatchDetailTab = MatchDetailTab.OVERVIEW,
    val isSubscribedToLive: Boolean = false,
    val isRefreshing: Boolean = false
)

enum class MatchDetailTab(val title: String) {
    OVERVIEW("Overview"),
    STATISTICS("Stats"),
    LINEUPS("Lineups"),
    FIXTURES("Fixtures"),
    STANDINGS("Standings"),
    HEAD_TO_HEAD("H2H")
}

sealed class MatchDetailAction {
    data class LoadMatch(val matchId: Int) : MatchDetailAction()
    data class LoadMatchEvents(val matchId: Int) : MatchDetailAction()
    data class LoadMatchFixtures(val leagueId: Int, val season: Int) : MatchDetailAction()
    data class LoadMatchLineups(val matchId: Int) : MatchDetailAction()
    data class LoadMatchStatistics(val matchId: Int) : MatchDetailAction()
    data class LoadHeadToHead(val homeTeamId: Int, val awayTeamId: Int) : MatchDetailAction()
    data class LoadStandings(val leagueId: Int, val season: Int) : MatchDetailAction()
    object RefreshMatch : MatchDetailAction()
    object RefreshAll : MatchDetailAction()
    data class SelectTab(val tab: MatchDetailTab) : MatchDetailAction()
    object ToggleLiveSubscription : MatchDetailAction()
    object ToggleFavorite : MatchDetailAction()
    object ShareMatch : MatchDetailAction()
    object NavigateBack : MatchDetailAction()
}

sealed class MatchDetailEffect {
    object NavigateBack : MatchDetailEffect()
    data class ShareMatch(val text: String) : MatchDetailEffect()
    data class ShowError(val message: String) : MatchDetailEffect()
    data class ShowSnackbar(val message: String) : MatchDetailEffect()
}