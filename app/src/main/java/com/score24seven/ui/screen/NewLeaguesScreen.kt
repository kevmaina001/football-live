/*
 * New Leagues Screen using Volley API - Creative UI with Working Backend
 */

package com.score24seven.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.score24seven.data.model.SimpleLeagueItem
import com.score24seven.data.model.SimpleStanding
import com.score24seven.ui.viewmodel.SimpleLeaguesViewModel
import com.score24seven.ui.viewmodel.SimpleLeaguesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLeaguesScreen(
    viewModel: SimpleLeaguesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showStandings) {
        StandingsDetailScreen(
            standings = uiState.standings,
            leagueName = uiState.selectedLeague?.third ?: "",
            isLoading = uiState.standingsLoading,
            error = uiState.standingsError,
            onBack = viewModel::clearStandings,
            onRetry = viewModel::retry
        )
    } else {
        LeaguesListScreen(
            uiState = uiState,
            onLeagueClick = { leagueItem ->
                val currentSeason = leagueItem.seasons?.find { it.current }
                if (currentSeason != null && leagueItem.league != null) {
                    viewModel.loadStandings(
                        leagueItem.league.id,
                        currentSeason.year,
                        leagueItem.league.name ?: "Unknown League"
                    )
                }
            },
            onRetry = viewModel::retry
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeaguesListScreen(
    uiState: SimpleLeaguesUiState,
    onLeagueClick: (SimpleLeagueItem) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "⚽ Football Leagues",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Discover leagues worldwide",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                IconButton(
                    onClick = onRetry,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White
                    )
                }
            }
        }

        when {
            uiState.isLoading -> {
                LoadingSection()
            }
            uiState.errorMessage != null -> {
                ErrorSection(
                    error = uiState.errorMessage,
                    onRetry = onRetry
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Priority Leagues Section
                    if (uiState.priorityLeagues.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "⭐ Top Leagues",
                                subtitle = "${uiState.priorityLeagues.size} major competitions"
                            )
                        }

                        items(uiState.priorityLeagues) { league ->
                            PriorityLeagueCard(
                                league = league,
                                onClick = { onLeagueClick(league) }
                            )
                        }
                    }

                    // Other Leagues Section
                    if (uiState.otherLeagues.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "🌍 More Leagues",
                                subtitle = "${uiState.otherLeagues.size} additional competitions"
                            )
                        }

                        items(uiState.otherLeagues) { league ->
                            StandardLeagueCard(
                                league = league,
                                onClick = { onLeagueClick(league) }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PriorityLeagueCard(
    league: SimpleLeagueItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority Badge
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // League Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = league.league?.name ?: "Unknown League",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = league.country?.name ?: "Unknown Country",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )

                val currentSeason = league.seasons?.find { it.current }
                if (currentSeason != null) {
                    Text(
                        text = "Season ${currentSeason.year}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Arrow
            Text(
                text = "›",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StandardLeagueCard(
    league: SimpleLeagueItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Country Flag/Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = league.country?.flag ?: "🏆",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // League Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = league.league?.name ?: "Unknown League",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = league.country?.name ?: "Unknown Country",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            // Arrow
            Text(
                text = "›",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading leagues...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ErrorSection(
    error: String,
    onRetry: () -> Unit
) {
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
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚠️",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Unable to load leagues",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Button(
                    onClick = onRetry,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Try Again")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StandingsDetailScreen(
    standings: List<SimpleStanding>,
    leagueName: String,
    isLoading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header
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
                        text = "League Standings",
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
                IconButton(onClick = onRetry) {
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
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                ErrorSection(error = error, onRetry = onRetry)
            }
            standings.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        StandingsHeaderRow()
                    }

                    itemsIndexed(standings) { index, standing ->
                        StandingRowCard(standing = standing, position = index + 1)
                    }
                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No standings available")
                }
            }
        }
    }
}

@Composable
private fun StandingsHeaderRow() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
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
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Team",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "P",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "W",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "D",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "L",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Pts",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StandingRowCard(standing: SimpleStanding, position: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = getPositionCardColor(position)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    .size(24.dp)
                    .background(
                        getPositionCircleColor(position),
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

            // Team name
            Text(
                text = standing.team?.name ?: "Unknown Team",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Stats
            Text(
                text = standing.all?.played?.toString() ?: "0",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.all?.win?.toString() ?: "0",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.all?.draw?.toString() ?: "0",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.all?.lose?.toString() ?: "0",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.points.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun getPositionCardColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50).copy(alpha = 0.1f) // Champions League - green
        in 5..6 -> Color(0xFFFFC107).copy(alpha = 0.1f) // Europa League - amber
        in 18..20 -> Color(0xFFF44336).copy(alpha = 0.1f) // Relegation - red
        else -> MaterialTheme.colorScheme.surface
    }
}

@Composable
private fun getPositionCircleColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50) // Champions League - green
        in 5..6 -> Color(0xFFFFC107) // Europa League - amber
        in 18..20 -> Color(0xFFF44336) // Relegation - red
        else -> Color(0xFF757575) // Default - gray
    }
}