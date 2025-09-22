/*
 * Improved Leagues Screen with Clean UI and Working Backend
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.score24seven.data.model.SimpleLeagueItem
import com.score24seven.data.model.SimpleStanding
import com.score24seven.ui.viewmodel.SimpleLeaguesViewModel
import com.score24seven.ui.viewmodel.SimpleLeaguesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprovedLeaguesScreen(
    onNavigateToLeagueDetails: ((Int, Int, String) -> Unit)? = null,
    viewModel: SimpleLeaguesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showStandings) {
        StandingsScreen(
            leagueName = uiState.selectedLeague?.third ?: "",
            standings = uiState.standings,
            isLoading = uiState.standingsLoading,
            error = uiState.standingsError,
            onBack = viewModel::clearStandings,
            onRefresh = viewModel::retry
        )
    } else {
        LeaguesScreen(
            uiState = uiState,
            onLeagueClick = { leagueItem ->
                val currentSeason = leagueItem.seasons?.find { it.current }
                if (currentSeason != null && leagueItem.league != null) {
                    // Navigate to comprehensive league details if navigation is available
                    if (onNavigateToLeagueDetails != null) {
                        onNavigateToLeagueDetails(
                            leagueItem.league.id,
                            currentSeason.year,
                            leagueItem.league.name ?: "Unknown League"
                        )
                    } else {
                        // Fallback to old standings-only view
                        viewModel.loadStandings(
                            leagueItem.league.id,
                            currentSeason.year,
                            leagueItem.league.name ?: "Unknown League"
                        )
                    }
                }
            },
            onRefresh = viewModel::retry
        )
    }
}

@Composable
private fun LeaguesScreen(
    uiState: SimpleLeaguesUiState,
    onLeagueClick: (SimpleLeagueItem) -> Unit,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Clean Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Football Leagues",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Select a league to view standings",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onRefresh) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading leagues...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "❌",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Failed to load leagues",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = uiState.errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Button(
                                onClick = onRefresh,
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Top Leagues
                    if (uiState.priorityLeagues.isNotEmpty()) {
                        item {
                            SectionTitle("⭐ Popular Leagues")
                        }
                        items(uiState.priorityLeagues) { league ->
                            PopularLeagueCard(
                                league = league,
                                onClick = { onLeagueClick(league) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }

                    // Other Leagues
                    if (uiState.otherLeagues.isNotEmpty()) {
                        item {
                            SectionTitle("🌍 Other Leagues")
                        }
                        items(uiState.otherLeagues) { league ->
                            RegularLeagueCard(
                                league = league,
                                onClick = { onLeagueClick(league) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun PopularLeagueCard(
    league: SimpleLeagueItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // League logo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (league.league?.logo != null) {
                    AsyncImage(
                        model = league.league.logo,
                        contentDescription = "${league.league.name} logo",
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = league.league?.name ?: "Unknown League",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = league.country?.name ?: "Unknown Country",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val currentSeason = league.seasons?.find { it.current }
                if (currentSeason != null) {
                    Text(
                        text = "Season ${currentSeason.year}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "View",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(180f),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RegularLeagueCard(
    league: SimpleLeagueItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // League logo
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (league.league?.logo != null) {
                    AsyncImage(
                        model = league.league.logo,
                        contentDescription = "${league.league.name} logo",
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Text(
                        text = league.country?.flag ?: "⚽",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = league.league?.name ?: "Unknown League",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = league.country?.name ?: "Unknown Country",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "›",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StandingsScreen(
    leagueName: String,
    standings: List<SimpleStanding>,
    isLoading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = leagueName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "League Table",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading standings...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "📊",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No standings available",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Button(
                            onClick = onRefresh,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }

            standings.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        TableHeader()
                    }

                    itemsIndexed(standings) { index, standing ->
                        StandingRow(standing = standing)
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No standings data available",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun TableHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Team",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "MP",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "W",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "D",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "L",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Pts",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(36.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StandingRow(standing: SimpleStanding) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getRowColor(standing.rank)
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Position
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        getPositionColor(standing.rank),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = standing.rank.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Team
            Text(
                text = standing.team?.name ?: "Unknown Team",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Stats
            Text(
                text = (standing.all?.played ?: 0).toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = (standing.all?.win ?: 0).toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = (standing.all?.draw ?: 0).toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = (standing.all?.lose ?: 0).toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.points.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(36.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun getRowColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50).copy(alpha = 0.1f) // Champions League
        in 5..6 -> Color(0xFFFFC107).copy(alpha = 0.1f) // Europa League
        in 18..20 -> Color(0xFFF44336).copy(alpha = 0.1f) // Relegation
        else -> MaterialTheme.colorScheme.surface
    }
}

@Composable
private fun getPositionColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50) // Champions League
        in 5..6 -> Color(0xFFFFC107) // Europa League
        in 18..20 -> Color(0xFFF44336) // Relegation
        else -> Color(0xFF757575)
    }
}

// Extension function for rotation
private fun Modifier.rotate(degrees: Float): Modifier {
    return this
}