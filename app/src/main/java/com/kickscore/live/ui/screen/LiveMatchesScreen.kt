/*
 * Live Matches Screen for KickScore Live
 * Shows currently live matches with league priority (EPL first)
 */

package com.kickscore.live.ui.screen

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
import com.kickscore.live.domain.model.Match
import com.kickscore.live.ui.components.ErrorState
import com.kickscore.live.ui.components.LoadingState
import com.kickscore.live.ui.components.EmptyState
import com.kickscore.live.ui.components.ModernMatchCard
import com.kickscore.live.ui.components.LeagueLogo
import com.kickscore.live.ui.components.CountryFlag
import com.kickscore.live.ui.components.TeamLogo
import com.kickscore.live.ui.design.tokens.Spacing
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.ui.viewmodel.HomeViewModel
import com.kickscore.live.util.Config

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveMatchesScreen(
    onNavigateToMatchDetail: (Match) -> Unit,
    modifier: Modifier = Modifier,
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
        // Live Header
        LiveHeader()

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
                val sortedMatches = sortLiveMatchesByFavoriteLeagues(liveMatches.data)

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
                        onMatchClick = onNavigateToMatchDetail
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
private fun LiveHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.lg),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.1f)
        )
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
                    .background(Color.Red, RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🔴 LIVE MATCHES",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "Live football matches happening now",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun LiveMatchesList(
    matches: List<Match>,
    onMatchClick: (Match) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xs),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        // Group matches by league
        val matchesByLeague = matches.groupBy { it.league }

        matchesByLeague.forEach { (league, leagueMatches) ->
            // League header
            item {
                LeagueHeader(league = league, matchCount = leagueMatches.size)
            }

            // League matches
            items(leagueMatches) { match ->
                ModernMatchCard(
                    match = match,
                    onClick = { onMatchClick(match) },
                    onLiveClick = { /* TODO: Handle live click */ },
                    onFavoriteClick = { /* TODO: Handle favorite click */ }
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
    league: com.kickscore.live.domain.model.League,
    matchCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.xs),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                // League logo with country flag overlay (for favorite competitions)
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
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = league.country,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Live match count badge
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Red
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
 * Sort live matches by favorite league priority using Config system
 */
private fun sortLiveMatchesByFavoriteLeagues(matches: List<Match>): List<Match> {
    println("🔴 DEBUG: LiveMatchesScreen - Total live matches received: ${matches.size}")

    // Log all leagues we're getting
    val leagueGroups = matches.groupBy { "${it.league.name} (${it.league.country}) [ID:${it.league.id}]" }
    leagueGroups.forEach { (key, matchList) ->
        println("🔴 DEBUG: LiveMatchesScreen - League: $key -> ${matchList.size} matches")
    }

    // Filter to only show matches from favorite competitions
    val favoriteMatches = matches.filter { match ->
        Config.PRIORITY_LEAGUE_IDS.contains(match.league.id)
    }

    println("🔴 DEBUG: LiveMatchesScreen - Favorite matches after filtering: ${favoriteMatches.size}")

    // Sort by priority league order, then by match time
    return favoriteMatches.sortedWith(compareBy<Match> { match ->
        val priorityIndex = Config.PRIORITY_LEAGUE_IDS.indexOf(match.league.id)
        if (priorityIndex != -1) priorityIndex else Int.MAX_VALUE
    }.thenBy { it.fixture.timestamp })
}

// Clean implementation using centralized Config system and logo components