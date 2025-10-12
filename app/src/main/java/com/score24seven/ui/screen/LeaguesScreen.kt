/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.score24seven.domain.model.League
import com.score24seven.domain.model.Standing
import com.score24seven.ui.state.UiState
import com.score24seven.ui.viewmodel.LeaguesViewModel
import com.score24seven.ui.viewmodel.LeagueTab
import com.score24seven.ui.components.TeamLogo
import com.score24seven.ads.BannerAdManager
import com.score24seven.ads.NativeAdManager
import com.score24seven.util.TeamNameUtils
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaguesScreen(
    viewModel: LeaguesViewModel = hiltViewModel()
) {
    println("🔥 DEBUG: LeaguesScreen - Screen composing")
    val state by viewModel.state.collectAsState()
    var showStandings by remember { mutableStateOf(false) }

    println("🔥 DEBUG: LeaguesScreen - State: leagues=${state.leagues}, selectedLeague=${state.selectedLeague?.name}")

    if (showStandings && state.selectedLeague != null) {
        StandingsScreen(
            league = state.selectedLeague!!,
            standingsState = state.standings,
            selectedTab = state.selectedTab,
            onTabSelected = viewModel::selectTab,
            onBack = {
                showStandings = false
            },
            onRefresh = viewModel::refreshData
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "🏆 Score24Seven Leagues (NEW VERSION)",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Banner Ad
            item {
                BannerAdManager.BannerAdView()
            }
            // Native Ad
            item {
                NativeAdManager.SimpleNativeAdCard()
            }


            val leaguesState = state.leagues
            println("🔥 DEBUG: LeaguesScreen - LeaguesState type: ${leaguesState::class.simpleName}")
            when (leaguesState) {
                is UiState.Loading -> {
                    println("🔥 DEBUG: LeaguesScreen - Showing loading state")
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is UiState.Error -> {
                    println("🔥 DEBUG: LeaguesScreen - Showing error state: ${leaguesState.message}")
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error Loading Leagues",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = leaguesState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                TextButton(
                                    onClick = viewModel::refreshData,
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                }

                is UiState.Success<*> -> {
                    val leagues = leaguesState.data as List<League>
                    println("🔥 DEBUG: LeaguesScreen - Showing success state with ${leagues.size} leagues")
                    leagues.forEach { league ->
                        println("🔥 DEBUG: League: ${league.name} (${league.country})")
                    }

                    if (leagues.isNotEmpty()) {
                        item {
                            Text(
                                text = "Popular Leagues",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        items(leagues.take(5)) { league ->
                            LeagueCard(
                                league = league,
                                onClick = {
                                    viewModel.selectLeague(league)
                                    showStandings = true
                                }
                            )
                        }

                        if (leagues.size > 5) {
                            item {
                                Text(
                                    text = "Other Competitions",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }

                            items(leagues.drop(5)) { league ->
                                LeagueCard(
                                    league = league,
                                    onClick = {
                                        viewModel.selectLeague(league)
                                        showStandings = true
                                    }
                                )
                            }
                        }
                    } else {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No Leagues Available",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Check back later for league data",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeagueCard(
    league: League,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag/Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = league.flag ?: "🏆",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // League info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = league.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = league.country,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Season: ${league.season ?: 2024}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Arrow or status
            Text(
                text = "›",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen(
    league: League,
    standingsState: UiState<List<Standing>>,
    selectedTab: LeagueTab,
    onTabSelected: (LeagueTab) -> Unit,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header with back button
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = league.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Season ${league.season ?: 2024}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                TextButton(onClick = onBack) {
                    Text("← Back")
                }
            },
            actions = {
                TextButton(onClick = onRefresh) {
                    Text("Refresh")
                }
            }
        )

        // Tab Row
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            LeagueTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    text = { Text(tab.title) }
                )
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            LeagueTab.STANDINGS -> {
                StandingsContent(
                    standingsState = standingsState,
                    onRefresh = onRefresh
                )
            }
            LeagueTab.TOP_SCORERS -> {
                // TODO: Implement top scorers when needed
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Top Scorers Coming Soon",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            LeagueTab.TEAMS -> {
                // TODO: Implement teams when needed
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Teams Coming Soon",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun StandingsContent(
    standingsState: UiState<List<Standing>>,
    onRefresh: () -> Unit
) {
    when (standingsState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error Loading Standings",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = standingsState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        TextButton(
                            onClick = onRefresh,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }

        is UiState.Success -> {
            val standings = standingsState.data

            if (standings.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        StandingsHeader()
                    }

                    // Banner Ad in Standings
                    item {
                        BannerAdManager.BannerAdView()
                    }

                    itemsIndexed(standings) { _, standing ->
                        StandingRow(
                            standing = standing,
                            onTeamClick = { teamId ->
                                // NAVIGATION: Navigate to team details
                                // This would need to be passed down from the parent composable
                                println("🏃 DEBUG: Team clicked: $teamId - ${standing.team.name}")
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Standings Available",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Standings data is not available for this league",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        TextButton(
                            onClick = onRefresh,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Refresh")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StandingsHeader() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Position
            Text(
                text = "#",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
            // Team - more space
            Text(
                text = "Team",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(start = 2.dp),
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
            // Compact stats
            Text(
                text = "P",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
            Text(
                text = "W",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
            Text(
                text = "D",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
            Text(
                text = "L",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
            Text(
                text = "GD",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(26.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
            Text(
                text = "Pts",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(26.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75f
            )
        }
    }
}

@Composable
private fun StandingRow(
    standing: Standing,
    onTeamClick: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTeamClick(standing.team.id) },
        colors = CardDefaults.cardColors(
            containerColor = getPositionColor(standing.rank)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Position - ultra compact
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        getPositionBackgroundColor(standing.rank),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = standing.rank.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.7f
                )
            }

            // Team logo and name - maximum space
            Row(
                modifier = Modifier.weight(1f).padding(start = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Always show team logo with proper fallback
                val logoUrl = standing.team.logo?.takeIf { it.isNotBlank() }
                    ?: "https://media.api-sports.io/football/teams/${standing.team.id}.png"

                TeamLogo(
                    logoUrl = logoUrl,
                    teamName = standing.team.name,
                    size = 14.dp
                )
                Text(
                    text = TeamNameUtils.getAbbreviatedName(standing.team.name, maxLength = 13),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.8f
                )
            }

            // Stats - ultra compact
            Text(
                text = standing.all.played.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.75f
            )
            Text(
                text = standing.all.win.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.75f
            )
            Text(
                text = standing.all.draw.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.75f
            )
            Text(
                text = standing.all.lose.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.75f
            )
            Text(
                text = if (standing.goalsDiff >= 0) "+${standing.goalsDiff}" else "${standing.goalsDiff}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(26.dp),
                textAlign = TextAlign.Center,
                color = if (standing.goalsDiff >= 0) Color.Green else Color.Red,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.75f
            )
            Text(
                text = standing.points.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(26.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.8f
            )
        }
    }
}

private fun getPositionColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50).copy(alpha = 0.1f) // Champions League - green
        in 5..6 -> Color(0xFFFFC107).copy(alpha = 0.1f) // Europa League - amber
        in 18..20 -> Color(0xFFF44336).copy(alpha = 0.1f) // Relegation - red
        else -> Color.Transparent
    }
}

private fun getPositionBackgroundColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50) // Champions League - green
        in 5..6 -> Color(0xFFFFC107) // Europa League - amber
        in 18..20 -> Color(0xFFF44336) // Relegation - red
        else -> Color(0xFF757575) // Default - gray
    }
}