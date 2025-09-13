/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.state

import com.kickscore.live.domain.model.Match

data class MatchDetailState(
    val match: UiState<Match> = UiState.Loading,
    val selectedTab: MatchDetailTab = MatchDetailTab.OVERVIEW,
    val isSubscribedToLive: Boolean = false,
    val isRefreshing: Boolean = false
)

enum class MatchDetailTab(val title: String) {
    OVERVIEW("Overview"),
    EVENTS("Events"),
    LINEUPS("Lineups"),
    STATISTICS("Stats"),
    HEAD_TO_HEAD("H2H")
}

sealed class MatchDetailAction {
    data class LoadMatch(val matchId: Int) : MatchDetailAction()
    object RefreshMatch : MatchDetailAction()
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