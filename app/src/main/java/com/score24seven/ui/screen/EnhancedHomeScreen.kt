/*
 * Enhanced Modern HomeScreen for Score24Seven
 */

package com.score24seven.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.score24seven.domain.model.Match
import com.score24seven.ui.components.ErrorState
import com.score24seven.ui.components.LoadingState
import com.score24seven.ui.components.EmptyState
import com.score24seven.ui.components.ModernMatchCard
import com.score24seven.ui.design.components.LeagueChip
import com.score24seven.ui.design.tokens.Spacing
import com.score24seven.ui.state.HomeScreenAction
import com.score24seven.ui.state.HomeScreenEffect
import com.score24seven.ui.state.UiState
import com.score24seven.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedHomeScreen(
    onNavigateToMatchDetail: (Int) -> Unit,
    onNavigateToMatches: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background
                    ),
                    startY = 0f,
                    endY = 300f
                )
            )
    ) {
        // Modern Header
        ModernHeader(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.handleAction(HomeScreenAction.RefreshData) }
        )

        // Featured Leagues
        FeaturedLeaguesSection(
            selectedLeague = state.selectedLeague,
            onLeagueSelected = { league ->
                viewModel.handleAction(HomeScreenAction.SelectLeague(league))
            }
        )

        // Tab Navigation
        ModernTabRow(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            onViewAllMatches = onNavigateToMatches
        )

        // Content based on selected tab
        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier.fillMaxSize(),
            label = "tab_content"
        ) { tabIndex ->
            when (tabIndex) {
                0 -> LiveMatchesContent(
                    matches = state.liveMatches,
                    onMatchClick = { match ->
                        viewModel.handleAction(HomeScreenAction.NavigateToMatch(match.id))
                    },
                    onLiveClick = { match ->
                        viewModel.handleAction(HomeScreenAction.SubscribeToLiveMatch(match.id))
                    },
                    onFavoriteClick = { match ->
                        viewModel.handleAction(HomeScreenAction.ToggleMatchFavorite(match.id))
                    }
                )
                1 -> TodayMatchesContent(
                    matches = state.todayMatches,
                    onMatchClick = { match ->
                        viewModel.handleAction(HomeScreenAction.NavigateToMatch(match.id))
                    },
                    onLiveClick = { match ->
                        viewModel.handleAction(HomeScreenAction.SubscribeToLiveMatch(match.id))
                    },
                    onFavoriteClick = { match ->
                        viewModel.handleAction(HomeScreenAction.ToggleMatchFavorite(match.id))
                    }
                )
                2 -> FavoritesContent()
            }
        }
    }
}

@Composable
private fun ModernHeader(
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.lg),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "⚽ Score24Seven",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Real-time Football Updates",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Refresh button with loading animation
            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
                    .size(48.dp)
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedLeaguesSection(
    selectedLeague: com.score24seven.domain.model.League?,
    onLeagueSelected: (com.score24seven.domain.model.League?) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = Spacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Featured Leagues",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = { /* TODO: Navigate to all leagues */ }) {
                Text("View All")
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            contentPadding = PaddingValues(vertical = Spacing.sm)
        ) {
            items(getFeaturedLeagues()) { league ->
                LeagueChip(
                    name = league,
                    isSelected = selectedLeague?.name == league,
                    onClick = { onLeagueSelected(null) } // TODO: Create league object
                )
            }
        }
    }
}

@Composable
private fun ModernTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onViewAllMatches: () -> Unit = {}
) {
    val tabs = listOf(
        "🔴 Live" to Icons.Default.PlayArrow,
        "📅 Today" to Icons.Default.Info,
        "❤️ Favorites" to Icons.Default.Favorite
    )

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.padding(horizontal = Spacing.lg),
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, (title, icon) ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.padding(vertical = Spacing.sm)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(Spacing.sm)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (selectedTabIndex == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                        Spacer(modifier = Modifier.width(Spacing.xs))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                    }
                }
            }
        }

        // View All Matches button
        OutlinedButton(
            onClick = onViewAllMatches,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("View All Matches")
        }
    }
}

@Composable
private fun LiveMatchesContent(
    matches: UiState<List<Match>>,
    onMatchClick: (Match) -> Unit,
    onLiveClick: (Match) -> Unit,
    onFavoriteClick: (Match) -> Unit
) {
    MatchContent(
        title = "Live Matches",
        matches = matches,
        onMatchClick = onMatchClick,
        onLiveClick = onLiveClick,
        onFavoriteClick = onFavoriteClick,
        emptyMessage = "No live matches at the moment"
    )
}

@Composable
private fun TodayMatchesContent(
    matches: UiState<List<Match>>,
    onMatchClick: (Match) -> Unit,
    onLiveClick: (Match) -> Unit,
    onFavoriteClick: (Match) -> Unit
) {
    MatchContent(
        title = "Today's Matches",
        matches = matches,
        onMatchClick = onMatchClick,
        onLiveClick = onLiveClick,
        onFavoriteClick = onFavoriteClick,
        emptyMessage = "No matches scheduled for today"
    )
}

@Composable
private fun FavoritesContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = "Your Favorites",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tap the heart icon on matches to save them here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun MatchContent(
    title: String,
    matches: UiState<List<Match>>,
    onMatchClick: (Match) -> Unit,
    onLiveClick: (Match) -> Unit,
    onFavoriteClick: (Match) -> Unit,
    emptyMessage: String
) {
    when (matches) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingState()
            }
        }

        is UiState.Success -> {
            if (matches.data.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(message = emptyMessage)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    items(matches.data) { match ->
                        ModernMatchCard(
                            match = match,
                            onClick = { onMatchClick(match) },
                            onLiveClick = { onLiveClick(match) },
                            onFavoriteClick = { onFavoriteClick(match) }
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorState(
                    message = matches.message,
                    onRetry = null
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
        "Europa League",
        "World Cup",
        "Euro 2024"
    )
}