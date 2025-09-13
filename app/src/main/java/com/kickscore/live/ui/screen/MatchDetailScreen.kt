/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.kickscore.live.ui.components.ErrorState
import com.kickscore.live.ui.components.LoadingState
import com.kickscore.live.ui.design.components.ScoreChip
import com.kickscore.live.ui.design.components.TeamRow
import com.kickscore.live.ui.design.tokens.KickScoreTypography
import com.kickscore.live.ui.design.tokens.Spacing
import com.kickscore.live.ui.state.MatchDetailAction
import com.kickscore.live.ui.state.MatchDetailEffect
import com.kickscore.live.ui.state.MatchDetailTab
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.ui.viewmodel.MatchDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: Int,
    onNavigateBack: () -> Unit,
    viewModel: MatchDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Load match data
    LaunchedEffect(matchId) {
        viewModel.handleAction(MatchDetailAction.LoadMatch(matchId))
    }

    // Handle effects
    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MatchDetailEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is MatchDetailEffect.ShareMatch -> {
                    // TODO: Handle share
                }
                is MatchDetailEffect.ShowError -> {
                    // TODO: Show error
                }
                is MatchDetailEffect.ShowSnackbar -> {
                    // TODO: Show snackbar
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (val matchState = state.match) {
                        is UiState.Success -> {
                            Text(
                                text = "${matchState.data.homeTeam.name} vs ${matchState.data.awayTeam.name}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        else -> {
                            Text("Match Details")
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.NavigateBack) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.ToggleFavorite) }
                    ) {
                        Icon(
                            imageVector = if (state.match.dataOrNull()?.isFavorite == true) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Favorite"
                        )
                    }

                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.ShareMatch) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val matchState = state.match) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorState(
                        message = matchState.message,
                        onRetry = {
                            viewModel.handleAction(MatchDetailAction.RefreshMatch)
                        }
                    )
                }
            }

            is UiState.Success -> {
                val match = matchState.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Match header
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.lg),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.md)
                    ) {
                        // League name
                        Text(
                            text = match.league.name,
                            style = KickScoreTypography.leagueName,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Teams and score
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TeamRow(
                                teamName = match.homeTeam.name,
                                logoUrl = match.homeTeam.logo,
                                isHome = true,
                                modifier = Modifier.weight(1f)
                            )

                            if (match.hasScore()) {
                                ScoreChip(
                                    homeScore = match.score.home!!,
                                    awayScore = match.score.away!!,
                                    isLive = match.status.isLive,
                                    modifier = Modifier.padding(horizontal = Spacing.lg)
                                )
                            } else {
                                Text(
                                    text = match.getTimeDisplay(),
                                    style = KickScoreTypography.scoreDisplay,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = Spacing.lg)
                                )
                            }

                            TeamRow(
                                teamName = match.awayTeam.name,
                                logoUrl = match.awayTeam.logo,
                                isHome = false,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Match status
                        Text(
                            text = "${match.status.long}${match.status.elapsed?.let { " - ${it}'" } ?: ""}",
                            style = KickScoreTypography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Tabs
                    TabRow(
                        selectedTabIndex = state.selectedTab.ordinal
                    ) {
                        MatchDetailTab.values().forEach { tab ->
                            Tab(
                                selected = state.selectedTab == tab,
                                onClick = {
                                    viewModel.handleAction(MatchDetailAction.SelectTab(tab))
                                },
                                text = { Text(tab.title) }
                            )
                        }
                    }

                    // Tab content
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(Spacing.lg)
                    ) {
                        when (state.selectedTab) {
                            MatchDetailTab.OVERVIEW -> {
                                OverviewTabContent(match)
                            }
                            MatchDetailTab.EVENTS -> {
                                EventsTabContent(match)
                            }
                            MatchDetailTab.LINEUPS -> {
                                LineupsTabContent(match)
                            }
                            MatchDetailTab.STATISTICS -> {
                                StatisticsTabContent(match)
                            }
                            MatchDetailTab.HEAD_TO_HEAD -> {
                                HeadToHeadTabContent(match)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OverviewTabContent(match: com.kickscore.live.domain.model.Match) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        Text(
            text = "Match Overview",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (match.venue != null) {
            Text(
                text = "Venue: ${match.venue.name}${match.venue.city?.let { ", $it" } ?: ""}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (match.referee != null) {
            Text(
                text = "Referee: ${match.referee}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = "Kick-off: ${match.fixture.dateTime}",
            style = MaterialTheme.typography.bodyLarge
        )

        // Add more overview content here
    }
}

@Composable
private fun EventsTabContent(match: com.kickscore.live.domain.model.Match) {
    Text(
        text = "Match Events - Coming Soon",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun LineupsTabContent(match: com.kickscore.live.domain.model.Match) {
    Text(
        text = "Match Lineups - Coming Soon",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun StatisticsTabContent(match: com.kickscore.live.domain.model.Match) {
    Text(
        text = "Match Statistics - Coming Soon",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun HeadToHeadTabContent(match: com.kickscore.live.domain.model.Match) {
    Text(
        text = "Head to Head - Coming Soon",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}