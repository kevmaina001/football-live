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
import com.score24seven.ui.state.TeamDetailTab
import com.score24seven.ui.state.TeamDetailAction
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
                        onAction = viewModel::handleAction,
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
    onAction: (TeamDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Team Header
        TeamHeaderCard(
            team = team,
            modifier = Modifier.padding(16.dp)
        )

        // Tab Row
        TabRow(
            selectedTabIndex = state.selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            TeamDetailTab.values().forEach { tab ->
                Tab(
                    selected = state.selectedTab == tab,
                    onClick = { onAction(TeamDetailAction.SelectTab(tab)) },
                    text = { Text(tab.title) }
                )
            }
        }

        // Tab Content
        when (state.selectedTab) {
            TeamDetailTab.OVERVIEW -> {
                TeamOverviewTab(team = team, state = state)
            }
            TeamDetailTab.FIXTURES -> {
                TeamFixturesTab(state = state)
            }
            TeamDetailTab.PLAYERS -> {
                TeamPlayersTab(state = state)
            }
        }
    }
}

@Composable
private fun TeamHeaderCard(team: Team, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
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

@Composable
private fun TeamOverviewTab(team: Team, state: TeamDetailState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Team Information",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Basic team info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    team.country?.let { country ->
                        Text(
                            text = "Country: $country",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    team.founded?.let { founded ->
                        Text(
                            text = "Founded: $founded",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    team.code?.let { code ->
                        Text(
                            text = "Code: $code",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // Recent/Immediate fixtures preview (3 most recent/immediate matches)
        when (val fixturesState = state.fixtures) {
            is UiState.Success<*> -> {
                val fixtures = fixturesState.data as List<Match>
                if (fixtures.isNotEmpty()) {
                    // Get the 3 most immediate fixtures (mix of recent played and upcoming)
                    val now = System.currentTimeMillis() / 1000
                    val recentPlayed = fixtures
                        .filter { it.fixture.timestamp < now }
                        .sortedByDescending { it.fixture.timestamp }
                        .take(2) // Last 2 played matches

                    val nextUpcoming = fixtures
                        .filter { it.fixture.timestamp >= now }
                        .sortedBy { it.fixture.timestamp }
                        .take(2) // Next 2 upcoming matches

                    val immediateFixtures = (recentPlayed + nextUpcoming).take(3)

                    if (immediateFixtures.isNotEmpty()) {
                        item {
                            Text(
                                text = "Recent & Upcoming",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        items(immediateFixtures) { match ->
                            MatchCard(match = match)
                        }
                    }
                }
            }
            else -> {
                item {
                    Text(
                        text = "Fixtures",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
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
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Loading fixtures...")
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TeamFixturesTab(state: TeamDetailState) {
    when (val fixturesState = state.fixtures) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error loading fixtures",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = fixturesState.message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success<*> -> {
            val fixtures = fixturesState.data as List<Match>
            if (fixtures.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No fixtures found",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "This team has no recent fixtures for the current season",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Separate played and upcoming matches
                val now = System.currentTimeMillis() / 1000
                val playedMatches = fixtures.filter { it.fixture.timestamp < now }
                val upcomingMatches = fixtures.filter { it.fixture.timestamp >= now }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Upcoming Matches Section
                    if (upcomingMatches.isNotEmpty()) {
                        item {
                            Text(
                                text = "Upcoming Fixtures (${upcomingMatches.size})",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        // Group upcoming matches by league
                        val upcomingByLeague = upcomingMatches.groupBy { it.league.name }
                        upcomingByLeague.forEach { (leagueName, matches) ->
                            item {
                                Text(
                                    text = leagueName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )
                            }
                            items(matches) { match ->
                                MatchCard(match = match)
                            }
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }

                    // Played Matches Section
                    if (playedMatches.isNotEmpty()) {
                        item {
                            Text(
                                text = "Recent Results (${playedMatches.size})",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        // Group played matches by league
                        val playedByLeague = playedMatches.reversed().groupBy { it.league.name }
                        playedByLeague.forEach { (leagueName, matches) ->
                            item {
                                Text(
                                    text = leagueName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )
                            }
                            items(matches) { match ->
                                MatchCard(match = match)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamPlayersTab(state: TeamDetailState) {
    when (val playersState = state.players) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error loading players",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = playersState.message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success<*> -> {
            val players = playersState.data as List<Player>
            if (players.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No players found",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Player information is not available for this team",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "Squad (${players.size} players)",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    items(players) { player ->
                        PlayerCard(player = player)
                    }
                }
            }
        }
    }
}