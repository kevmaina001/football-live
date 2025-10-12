/*
 * Live TV Screen for Score24Seven
 * Shows currently live matches with favorite competition filtering
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.score24seven.ui.components.LeagueLogo
import com.score24seven.ui.components.CountryFlag
import com.score24seven.ui.design.tokens.Spacing
import com.score24seven.ui.state.UiState
import com.score24seven.ui.viewmodel.HomeViewModel
import com.score24seven.util.Config
import com.score24seven.ads.BannerAdManager
import com.score24seven.ads.NativeAdManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTvScreen(
    modifier: Modifier = Modifier,
    onMatchClick: (Match) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
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
        // Live TV Header
        LiveTvHeader()

        // Live matches content
        when (val liveMatches = state.liveMatches) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState(message = "Loading live matches...")
                }
            }
            is UiState.Success -> {
                val sortedMatches = sortAllLiveMatchesWithFavoritesPinned(liveMatches.data)

                if (sortedMatches.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyState(
                            message = "No live matches at the moment",
                            actionText = "Refresh",
                            onAction = {
                                // TODO: Implement refresh
                            }
                        )
                    }
                } else {
                    LiveMatchesList(
                        matches = sortedMatches,
                        onMatchClick = onMatchClick,
                        onToggleFavorite = { matchId ->
                            viewModel.handleAction(com.score24seven.ui.state.HomeScreenAction.ToggleMatchFavorite(matchId))
                        }
                    )
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorState(
                        message = liveMatches.message,
                        onRetry = {
                            // TODO: Implement retry
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveTvHeader() {
    val liveColor = Color(0xFFE53E3E) // Consistent red color

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.lg),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live indicator with pulsing animation
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(liveColor, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🔴 LIVE TV",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = "All live football matches (favorites pinned)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = liveColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun LiveMatchesList(
    matches: List<Match>,
    onMatchClick: (Match) -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xs),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        // Banner Ad at top
        item {
            BannerAdManager.BannerAdView()
        }
        // Native Ad
        item {
            NativeAdManager.SimpleNativeAdCard()
        }


        // Group matches by league
        val matchesByLeague = matches.groupBy { it.league }

        matchesByLeague.entries.toList().forEachIndexed { index, entry ->
            val league = entry.key
            val leagueMatches = entry.value

            // League header
            item {
                LeagueHeader(league = league, matchCount = leagueMatches.size)
            }

            // League matches
            items(leagueMatches) { match ->
                ModernMatchCard(
                    match = match,
                    onClick = { onMatchClick(match) },
                    onLiveClick = { onMatchClick(match) },
                    onFavoriteClick = { onToggleFavorite(match.id) }
                )
            }

            // Spacing between leagues
            item {
                Spacer(modifier = Modifier.height(Spacing.md))
            }
        }
    }
}

@Composable
private fun LeagueHeader(
    league: com.score24seven.domain.model.League,
    matchCount: Int
) {
    val liveColor = Color(0xFFE53E3E) // Consistent red color

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.xs),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // League logo with country flag overlay
                Box {
                    LeagueLogo(
                        logoUrl = league.logo,
                        leagueName = league.name,
                        size = 32.dp
                    )

                    // Small country flag overlay in bottom-right corner
                    CountryFlag(
                        flagUrl = league.flag,
                        countryName = league.country,
                        size = 12.dp,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }

                // League name
                Column {
                    Text(
                        text = league.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = league.country,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Live match count badge
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = liveColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.White, RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$matchCount LIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Sort ALL live matches with favorite competitions pinned at the top
 */
private fun sortAllLiveMatchesWithFavoritesPinned(matches: List<Match>): List<Match> {
    println("🔴 DEBUG: LiveTvScreen - Total live matches received: ${matches.size}")

    // Log all leagues we're getting
    val leagueGroups = matches.groupBy { "${it.league.name} (${it.league.country}) [ID:${it.league.id}]" }
    leagueGroups.forEach { (key, matchList) ->
        println("🔴 DEBUG: LiveTvScreen - League: $key -> ${matchList.size} matches")
    }

    // Separate favorite and other matches
    val favoriteMatches = matches.filter { match ->
        Config.PRIORITY_LEAGUE_IDS.contains(match.league.id)
    }

    val otherMatches = matches.filter { match ->
        !Config.PRIORITY_LEAGUE_IDS.contains(match.league.id)
    }

    println("🔴 DEBUG: LiveTvScreen - Favorite matches (pinned): ${favoriteMatches.size}")
    println("🔴 DEBUG: LiveTvScreen - Other matches: ${otherMatches.size}")

    // Sort favorites by priority order, then by match time
    val sortedFavorites = favoriteMatches.sortedWith(compareBy<Match> { match ->
        val priorityIndex = Config.PRIORITY_LEAGUE_IDS.indexOf(match.league.id)
        if (priorityIndex != -1) priorityIndex else Int.MAX_VALUE
    }.thenBy { it.fixture.timestamp })

    // Sort other matches by league name, then by match time
    val sortedOthers = otherMatches.sortedWith(compareBy<Match> { match ->
        match.league.name
    }.thenBy { it.fixture.timestamp })

    // Combine: favorites first (pinned), then others
    val finalList = sortedFavorites + sortedOthers

    println("🔴 DEBUG: LiveTvScreen - Final list: ${finalList.size} matches (${sortedFavorites.size} pinned + ${sortedOthers.size} others)")

    return finalList
}