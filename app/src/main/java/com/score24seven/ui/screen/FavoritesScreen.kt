/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.score24seven.R
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.isLive
import com.score24seven.ui.components.ModernMatchCard
import com.score24seven.ui.components.LoadingState
import com.score24seven.ui.components.EmptyState
import com.score24seven.ui.state.UiState
import com.score24seven.ui.viewmodel.FavoritesViewModel
import com.score24seven.ads.BannerAdManager
import com.score24seven.ads.NativeAdManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onNavigateToMatchDetail: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63).copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background
                    ),
                    startY = 0f,
                    endY = 400f
                )
            )
    ) {
        // Hero Header
        FavoritesHeader(
            favoriteCount = when (val favState = state.favoriteMatches) {
                is UiState.Success -> favState.data.size
                else -> 0
            },
            liveCount = when (val favState = state.favoriteMatches) {
                is UiState.Success -> favState.data.count { it.isLive() }
                else -> 0
            },
            onRefresh = { viewModel.refreshFavorites() }
        )

        // Filter Tabs
        FavoriteFilterTabs(
            selectedFilter = state.selectedFilter,
            onFilterSelected = { viewModel.setFilter(it) },
            liveCount = when (val favState = state.favoriteMatches) {
                is UiState.Success -> favState.data.count { it.isLive() }
                else -> 0
            },
            upcomingCount = when (val favState = state.favoriteMatches) {
                is UiState.Success -> favState.data.count { !it.isLive() && it.status.short == "NS" }
                else -> 0
            },
            finishedCount = when (val favState = state.favoriteMatches) {
                is UiState.Success -> favState.data.count { it.status.isFinished }
                else -> 0
            }
        )

        // Favorites List
        when (val favState = state.favoriteMatches) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState(message = "Loading your favorites...")
                }
            }
            is UiState.Success -> {
                val filteredMatches = when (state.selectedFilter) {
                    FavoriteFilter.ALL -> favState.data
                    FavoriteFilter.LIVE -> favState.data.filter { it.isLive() }
                    FavoriteFilter.UPCOMING -> favState.data.filter { !it.isLive() && it.status.short == "NS" }
                    FavoriteFilter.FINISHED -> favState.data.filter { it.status.isFinished }
                }

                if (filteredMatches.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyState(
                            message = if (favState.data.isEmpty()) {
                                "No favorite matches yet\nStart adding matches to your favorites!"
                            } else {
                                "No ${state.selectedFilter.name.lowercase()} matches"
                            },
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Banner Ad at top
                        item {
                            BannerAdManager.BannerAdView()
                        }
                        // Native Ad
                        item {
                            NativeAdManager.SimpleNativeAdCard()
                        }


                        items(filteredMatches, key = { it.id }) { match ->
                            ModernMatchCard(
                                match = match,
                                onClick = { onNavigateToMatchDetail(match.id) },
                                onLiveClick = { onNavigateToMatchDetail(match.id) },
                                onFavoriteClick = { viewModel.toggleFavorite(match.id) }
                            )
                        }

                        // Bottom spacing
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorCard(
                        title = "Failed to Load Favorites",
                        message = favState.message ?: "Unable to load favorite matches",
                        onRetry = { viewModel.refreshFavorites() }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesHeader(
    favoriteCount: Int,
    liveCount: Int,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE91E63).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Heart Icon with gradient background
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE91E63),
                                    Color(0xFFF06292)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column {
                    Text(
                        text = "My Favorites",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63)
                    )
                    Text(
                        text = "$favoriteCount matches • $liveCount live",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Color(0xFFE91E63)
                )
            }
        }
    }
}

@Composable
private fun FavoriteFilterTabs(
    selectedFilter: FavoriteFilter,
    onFilterSelected: (FavoriteFilter) -> Unit,
    liveCount: Int,
    upcomingCount: Int,
    finishedCount: Int
) {
    ScrollableTabRow(
        selectedTabIndex = selectedFilter.ordinal,
        modifier = Modifier.fillMaxWidth(),
        edgePadding = 16.dp,
        containerColor = Color.Transparent,
        contentColor = Color(0xFFE91E63),
    ) {
        Tab(
            selected = selectedFilter == FavoriteFilter.ALL,
            onClick = { onFilterSelected(FavoriteFilter.ALL) },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("All")
                    if (liveCount + upcomingCount + finishedCount > 0) {
                        Badge(
                            containerColor = if (selectedFilter == FavoriteFilter.ALL)
                                Color(0xFFE91E63) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text("${liveCount + upcomingCount + finishedCount}")
                        }
                    }
                }
            }
        )
        Tab(
            selected = selectedFilter == FavoriteFilter.LIVE,
            onClick = { onFilterSelected(FavoriteFilter.LIVE) },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("Live")
                    if (liveCount > 0) {
                        Badge(
                            containerColor = if (selectedFilter == FavoriteFilter.LIVE)
                                Color(0xFFE53E3E) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text("$liveCount")
                        }
                    }
                }
            }
        )
        Tab(
            selected = selectedFilter == FavoriteFilter.UPCOMING,
            onClick = { onFilterSelected(FavoriteFilter.UPCOMING) },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("Upcoming")
                    if (upcomingCount > 0) {
                        Badge(
                            containerColor = if (selectedFilter == FavoriteFilter.UPCOMING)
                                Color(0xFF1976D2) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text("$upcomingCount")
                        }
                    }
                }
            }
        )
        Tab(
            selected = selectedFilter == FavoriteFilter.FINISHED,
            onClick = { onFilterSelected(FavoriteFilter.FINISHED) },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("Finished")
                    if (finishedCount > 0) {
                        Badge(
                            containerColor = if (selectedFilter == FavoriteFilter.FINISHED)
                                Color(0xFF2E7D32) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text("$finishedCount")
                        }
                    }
                }
            }
        )
    }
}

enum class FavoriteFilter {
    ALL, LIVE, UPCOMING, FINISHED
}
