/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.score24seven.domain.model.*
import com.score24seven.ui.state.UiState
import com.score24seven.ui.state.TeamDetailState
import com.score24seven.ui.viewmodel.TeamDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    teamId: Int,
    onNavigateBack: () -> Unit,
    viewModel: TeamDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.loadTeamDetails(teamId)
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (val teamState = state.team) {
                                is UiState.Success<*> -> (teamState.data as Team).name
                                else -> "Team Details"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.toggleFavorite() }
                        ) {
                            Icon(
                                imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites",
                                tint = if (state.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { viewModel.shareTeam() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share team"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            when (val teamState = state.team) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error loading team details",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = teamState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadTeamDetails(teamId) }
                        ) {
                            Text("Retry")
                        }
                    }
                }
                is UiState.Success<*> -> {
                    TeamDetailContent(
                        team = teamState.data as Team,
                        state = state,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamDetailContent(
    team: Team,
    state: TeamDetailState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Team Header
        item {
            TeamHeaderCard(team = team)
        }

        // Team Statistics
        when (val statsState = state.statistics) {
            is UiState.Success<*> -> {
                item {
                    TeamStatisticsCard(statistics = statsState.data as TeamStatistics)
                }
            }
            is UiState.Loading -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            is UiState.Error -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Error loading statistics",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // Recent Fixtures
        when (val fixturesState = state.fixtures) {
            is UiState.Success<*> -> {
                if ((fixturesState.data as List<Match>).isNotEmpty()) {
                    item {
                        Text(
                            text = "Recent Fixtures",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    items((fixturesState.data as List<Match>).take(5)) { match ->
                        MatchCard(match = match)
                    }
                }
            }
            is UiState.Loading -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            else -> {} // Error state - skip fixtures section
        }

        // Players Section (if available)
        when (val playersState = state.players) {
            is UiState.Success<*> -> {
                if ((playersState.data as List<Player>).isNotEmpty()) {
                    item {
                        Text(
                            text = "Squad",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    items((playersState.data as List<Player>).take(10)) { player ->
                        PlayerCard(player = player)
                    }
                }
            }
            is UiState.Loading -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            else -> {} // Error state - skip players section
        }
    }
}

@Composable
private fun TeamHeaderCard(team: Team) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Team Logo
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(team.logo)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .build(),
                contentDescription = "${team.name} logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Team Name
            Text(
                text = team.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )

            // Team Details
            team.country?.let { country ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = country,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                )
            }

            team.founded?.let { founded ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Founded: $founded",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}

@Composable
private fun TeamStatisticsCard(statistics: TeamStatistics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Form
            if (statistics.form.isNotEmpty()) {
                StatisticRow("Form", statistics.form)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Games Played
            StatisticRow("Games Played", "${statistics.fixtures.played.total ?: 0}")
            Spacer(modifier = Modifier.height(8.dp))

            // Wins/Draws/Losses
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem("Wins", "${statistics.fixtures.wins.total ?: 0}", Color.Green)
                StatisticItem("Draws", "${statistics.fixtures.draws.total ?: 0}", Color.Gray)
                StatisticItem("Losses", "${statistics.fixtures.loses.total ?: 0}", Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Goals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem("Goals For", "${statistics.goals.goalsFor.total ?: 0}", MaterialTheme.colorScheme.primary)
                StatisticItem("Goals Against", "${statistics.goals.goalsAgainst.total ?: 0}", MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun StatisticRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun StatisticItem(label: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MatchCard(match: Match) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Team
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = match.homeTeam.logo,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    placeholder = androidx.compose.ui.graphics.painter.ColorPainter(Color.Gray)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = match.homeTeam.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            // Score
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                if (match.hasScore()) {
                    Text(
                        text = "${match.score.home} - ${match.score.away}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = match.status.short,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Away Team
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = match.awayTeam.logo,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    placeholder = androidx.compose.ui.graphics.painter.ColorPainter(Color.Gray)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = match.awayTeam.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PlayerCard(player: Player) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Photo
            AsyncImage(
                model = player.photo,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                placeholder = androidx.compose.ui.graphics.painter.ColorPainter(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Player Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                player.position?.let { position ->
                    Text(
                        text = position,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Player Number
            player.number?.let { number ->
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = number.toString(),
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
    }
}