/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kickscore.live.domain.model.Match
import com.kickscore.live.ui.components.MatchCard
import com.kickscore.live.ui.components.LoadingState
import com.kickscore.live.ui.components.ErrorState
import com.kickscore.live.ui.components.EmptyState
import com.kickscore.live.ui.design.components.LeagueChip
import com.kickscore.live.ui.design.tokens.Spacing
import com.kickscore.live.ui.state.HomeScreenAction
import com.kickscore.live.ui.state.HomeScreenEffect
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToMatchDetail: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    // Handle effects
    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is HomeScreenEffect.NavigateToMatchDetail -> {
                    onNavigateToMatchDetail(effect.matchId)
                }
                is HomeScreenEffect.ShowError -> {
                    // TODO: Show error snackbar
                }
                is HomeScreenEffect.ShowSnackbar -> {
                    // TODO: Show snackbar
                }
            }
        }
    }

    // Handle pull to refresh
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.handleAction(HomeScreenAction.RefreshData)
            pullToRefreshState.endRefresh()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.Screen.horizontal),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "KickScore Live",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            viewModel.handleAction(HomeScreenAction.RefreshData)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // Featured Leagues
            item {
                Column {
                    Text(
                        text = "Featured Leagues",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = Spacing.Screen.horizontal)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = Spacing.Screen.horizontal),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                        modifier = Modifier.padding(top = Spacing.md)
                    ) {
                        items(getFeaturedLeagues()) { league ->
                            LeagueChip(
                                name = league,
                                isSelected = state.selectedLeague?.name == league,
                                onClick = {
                                    // TODO: Handle league selection
                                }
                            )
                        }
                    }
                }
            }

            // Live Matches Section
            item {
                MatchSection(
                    title = "Live Matches",
                    matches = state.liveMatches,
                    onMatchClick = { match ->
                        viewModel.handleAction(HomeScreenAction.NavigateToMatch(match.id))
                    },
                    onSubscribeToLive = { match ->
                        viewModel.handleAction(HomeScreenAction.SubscribeToLiveMatch(match.id))
                    },
                    emptyMessage = "No live matches at the moment"
                )
            }

            // Today's Matches Section
            item {
                MatchSection(
                    title = "Today's Matches",
                    matches = state.todayMatches,
                    onMatchClick = { match ->
                        viewModel.handleAction(HomeScreenAction.NavigateToMatch(match.id))
                    },
                    onSubscribeToLive = { match ->
                        viewModel.handleAction(HomeScreenAction.SubscribeToLiveMatch(match.id))
                    },
                    emptyMessage = "No matches scheduled for today"
                )
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}

@Composable
private fun MatchSection(
    title: String,
    matches: UiState<List<Match>>,
    onMatchClick: (Match) -> Unit,
    onSubscribeToLive: (Match) -> Unit,
    emptyMessage: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = Spacing.Screen.horizontal)
        )

        when (matches) {
            is UiState.Loading -> {
                LoadingState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.xl)
                )
            }

            is UiState.Success -> {
                if (matches.data.isEmpty()) {
                    EmptyState(
                        message = emptyMessage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.xl)
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = Spacing.Screen.horizontal),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                        modifier = Modifier.padding(top = Spacing.md)
                    ) {
                        items(matches.data) { match ->
                            MatchCard(
                                match = match,
                                onClick = { onMatchClick(match) },
                                onLiveClick = { onSubscribeToLive(match) },
                                modifier = Modifier.fillParentMaxWidth(0.85f)
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                ErrorState(
                    message = matches.message,
                    onRetry = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.xl)
                )
            }
        }
    }
}

private fun getFeaturedLeagues(): List<String> {
    return listOf(
        "Premier League",
        "Champions League",
        "La Liga",
        "Serie A",
        "Bundesliga",
        "Ligue 1",
        "Europa League"
    )
}