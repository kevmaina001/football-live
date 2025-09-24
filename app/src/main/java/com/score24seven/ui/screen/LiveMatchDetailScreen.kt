/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.score24seven.domain.usecase.*
import com.score24seven.domain.model.TeamStatistics
import com.score24seven.ui.components.ErrorState
import com.score24seven.ui.components.LoadingState
import com.score24seven.ui.design.components.TeamRow
import com.score24seven.ui.design.tokens.Spacing
import com.score24seven.ui.state.*
import com.score24seven.ui.viewmodel.MatchDetailViewModel
import com.score24seven.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveMatchDetailScreen(
    matchId: Int,
    onNavigateBack: () -> Unit,
    viewModel: MatchDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.handleAction(MatchDetailAction.LoadMatch(matchId))
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MatchDetailEffect.NavigateBack -> onNavigateBack()
                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF0A1628),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Match Details",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.NavigateBack) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.ShareMatch) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A1628)
                )
            )
        }
    ) { paddingValues ->
        when (val matchState = state.match) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFF0A1628)),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFF0A1628)),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorState(
                        message = matchState.message,
                        onRetry = { viewModel.handleAction(MatchDetailAction.RefreshMatch) }
                    )
                }
            }

            is UiState.Success -> {
                LiveMatchDetailContent(
                    match = matchState.data,
                    state = state,
                    onAction = viewModel::handleAction,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun LiveMatchDetailContent(
    match: Match,
    state: MatchDetailState,
    onAction: (MatchDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628))
    ) {
        // Match Header
        LiveMatchHeaderSection(match = match)

        // Tab Navigation
        ScrollableTabRow(
            selectedTabIndex = state.selectedTab.ordinal,
            containerColor = Color(0xFF0A1628),
            contentColor = Color.White,
            indicator = { tabPositions ->
                if (tabPositions.isNotEmpty()) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier,
                        color = Color(0xFFFFB000)
                    )
                }
            },
            divider = {}
        ) {
            MatchDetailTab.values().forEach { tab ->
                Tab(
                    selected = state.selectedTab == tab,
                    onClick = { onAction(MatchDetailAction.SelectTab(tab)) },
                    text = {
                        Text(
                            text = tab.title,
                            color = if (state.selectedTab == tab) Color(0xFFFFB000) else Color(0xFF9CA3AF),
                            fontWeight = if (state.selectedTab == tab) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A1628))
                .padding(16.dp)
        ) {
            when (state.selectedTab) {
                MatchDetailTab.OVERVIEW -> {
                    LiveOverviewTab(match)
                }
                MatchDetailTab.FIXTURES -> {
                    PlaceholderTab("League Fixtures", "Previous and upcoming fixtures will appear here")
                }
                MatchDetailTab.LINEUPS -> {
                    PlaceholderTab("Team Lineups", "Starting lineups and formations will be displayed here")
                }
                MatchDetailTab.STATISTICS -> {
                    PlaceholderTab("Match Statistics", "Possession, shots, passes and other match statistics")
                }
                MatchDetailTab.HEAD_TO_HEAD -> {
                    PlaceholderTab("Head to Head", "Previous matches between ${match.homeTeam.name} and ${match.awayTeam.name}")
                }
                else -> {
                    PlaceholderTab("Coming Soon", "This feature will be available soon")
                }
            }
        }
    }
}

@Composable
private fun LiveMatchHeaderSection(match: Match) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${match.league.country} • ${match.league.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF),
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    TeamRow(
                        teamName = match.homeTeam.name,
                        logoUrl = match.homeTeam.logo,
                        isHome = true,
                        compact = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    if (match.hasScore()) {
                        Text(
                            text = "${match.score.home ?: 0} - ${match.score.away ?: 0}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        )
                    } else {
                        Text(
                            text = match.getTimeDisplay(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Text(
                        text = match.status.long,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (match.status.isLive) Color(0xFF10B981) else Color(0xFF9CA3AF),
                            fontSize = 12.sp
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    TeamRow(
                        teamName = match.awayTeam.name,
                        logoUrl = match.awayTeam.logo,
                        isHome = false,
                        compact = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            match.status.elapsed?.let { elapsed ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${elapsed}'",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun LiveOverviewTab(match: Match) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LiveInfoCard(
                title = "Match Information",
                items = buildList {
                    match.fixture.dateTime?.let {
                        add("Kick-off" to it.toString())
                    }
                    match.venue?.let { venue ->
                        add("Venue" to "${venue.name}${venue.city?.let { ", $it" } ?: ""}")
                    }
                    match.referee?.let {
                        add("Referee" to it)
                    }
                    add("Season" to match.league.season.toString())
                    match.league.round?.let {
                        add("Round" to it)
                    }
                }
            )
        }

        if (match.hasScore()) {
            item {
                LiveInfoCard(
                    title = "Score Details",
                    items = listOf(
                        "Halftime" to "${match.score.halftime?.home ?: 0} - ${match.score.halftime?.away ?: 0}",
                        "Fulltime" to "${match.score.fulltime?.home ?: 0} - ${match.score.fulltime?.away ?: 0}",
                        "Extra Time" to "${match.score.extratime?.home ?: 0} - ${match.score.extratime?.away ?: 0}"
                    )
                )
            }
        }
    }
}

@Composable
private fun PlaceholderTab(title: String, description: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LiveEventsTab(events: UiState<List<com.score24seven.domain.usecase.MatchEvent>>) {
    when (events) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingState()
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No match events available",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }
        }
        is UiState.Success -> {
            if (events.data.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No events recorded yet",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sortedEvents = events.data.sortedByDescending { it.minute }
                    items(
                        items = sortedEvents,
                        key = { it.minute }
                    ) { event ->
                        EventCard(event = event)
                    }
                }
            }
        }
    }
}

@Composable
private fun EventCard(event: com.score24seven.domain.usecase.MatchEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${event.minute}'",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(40.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = event.detail,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "${event.player}${event.assist?.let { " (Assist: $it)" } ?: ""}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
                Text(
                    text = event.team,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF6B7280)
                    )
                )
            }
        }
    }
}

@Composable
private fun LiveLineupsTab(lineups: UiState<List<TeamLineup>>) {
    when (lineups) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingState()
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Lineups not available yet",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }
        }
        is UiState.Success -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(lineups.data) { lineup ->
                    TeamLineupCard(lineup = lineup)
                }
            }
        }
    }
}

@Composable
private fun TeamLineupCard(lineup: TeamLineup) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                TeamRow(
                    teamName = lineup.teamName,
                    logoUrl = lineup.teamLogo,
                    isHome = true,
                    compact = true,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = lineup.formation,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFFFFB000),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Text(
                text = "Coach: ${lineup.coach}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Starting XI",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            lineup.startingXI.forEach { player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "${player.number}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFFFB000),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.width(30.dp)
                    )
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = player.position,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveStatisticsTab(statistics: UiState<List<TeamStatistics>>) {
    when (statistics) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingState()
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Statistics not available yet",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }
        }
        is UiState.Success -> {
            if (statistics.data.size >= 2) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val homeStats = statistics.data[0]
                    val awayStats = statistics.data[1]

                    // Display key statistics
                    item {
                        StatisticComparisonRow(
                            statName = "Games Played",
                            homeValue = homeStats.fixtures.played.total?.toString() ?: "0",
                            awayValue = awayStats.fixtures.played.total?.toString() ?: "0"
                        )
                    }

                    item {
                        StatisticComparisonRow(
                            statName = "Wins",
                            homeValue = homeStats.fixtures.wins.total?.toString() ?: "0",
                            awayValue = awayStats.fixtures.wins.total?.toString() ?: "0"
                        )
                    }

                    item {
                        StatisticComparisonRow(
                            statName = "Draws",
                            homeValue = homeStats.fixtures.draws.total?.toString() ?: "0",
                            awayValue = awayStats.fixtures.draws.total?.toString() ?: "0"
                        )
                    }

                    item {
                        StatisticComparisonRow(
                            statName = "Losses",
                            homeValue = homeStats.fixtures.loses.total?.toString() ?: "0",
                            awayValue = awayStats.fixtures.loses.total?.toString() ?: "0"
                        )
                    }

                    item {
                        StatisticComparisonRow(
                            statName = "Goals For",
                            homeValue = homeStats.goals.goalsFor.total?.toString() ?: "0",
                            awayValue = awayStats.goals.goalsFor.total?.toString() ?: "0"
                        )
                    }

                    item {
                        StatisticComparisonRow(
                            statName = "Goals Against",
                            homeValue = homeStats.goals.goalsAgainst.total?.toString() ?: "0",
                            awayValue = awayStats.goals.goalsAgainst.total?.toString() ?: "0"
                        )
                    }

                    item {
                        StatisticComparisonRow(
                            statName = "Clean Sheets",
                            homeValue = homeStats.cleanSheet.total?.toString() ?: "0",
                            awayValue = awayStats.cleanSheet.total?.toString() ?: "0"
                        )
                    }

                    if (homeStats.form.isNotEmpty() && awayStats.form.isNotEmpty()) {
                        item {
                            StatisticComparisonRow(
                                statName = "Form",
                                homeValue = homeStats.form,
                                awayValue = awayStats.form
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticComparisonRow(
    statName: String,
    homeValue: String,
    awayValue: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = homeValue,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )

            Text(
                text = statName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFFFB000)
                ),
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center
            )

            Text(
                text = awayValue,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun LiveHeadToHeadTab(headToHead: UiState<HeadToHeadData>) {
    when (headToHead) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingState()
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Head-to-head data not available",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }
        }
        is UiState.Success -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                headToHead.data.predictions?.let { prediction ->
                    item {
                        PredictionCard(prediction = prediction)
                    }
                }

                if (headToHead.data.recentMatches.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recent Matches",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color(0xFFFFB000),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    items(headToHead.data.recentMatches) { match ->
                        RecentMatchCard(match = match)
                    }
                }
            }
        }
    }
}

@Composable
private fun PredictionCard(prediction: MatchPrediction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Match Prediction",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            prediction.winner?.let {
                Text(
                    text = "Predicted Winner: $it",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White
                    )
                )
            }

            prediction.advice?.let {
                Text(
                    text = "Advice: $it",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                prediction.homePercent?.let {
                    Text(
                        text = "Home: $it",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
                prediction.drawPercent?.let {
                    Text(
                        text = "Draw: $it",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
                prediction.awayPercent?.let {
                    Text(
                        text = "Away: $it",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentMatchCard(match: RecentMatch) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = match.homeTeam,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                    Text(
                        text = "${match.homeScore}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = match.awayTeam,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                    Text(
                        text = "${match.awayScore}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Text(
                    text = "${match.league} • ${match.date}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }
        }
    }
}

@Composable
private fun LiveInfoCard(
    title: String,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}