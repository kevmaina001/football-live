/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.score24seven.R
import com.score24seven.ui.components.ErrorState
import com.score24seven.ui.components.LoadingState
import com.score24seven.ui.design.components.ScoreChip
import com.score24seven.ui.design.components.TeamRow
import com.score24seven.ui.design.tokens.Score24SevenTypography
import com.score24seven.ui.design.tokens.Spacing
import com.score24seven.ui.state.*
import com.score24seven.ui.viewmodel.MatchDetailViewModel
import com.score24seven.domain.model.*
import androidx.compose.foundation.isSystemInDarkTheme
import com.score24seven.ads.BannerAdManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMatchDetailScreen(
    matchId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToTeamDetail: (Int) -> Unit = {},
    viewModel: MatchDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Load match data
    LaunchedEffect(matchId) {
        viewModel.handleAction(MatchDetailAction.LoadMatch(matchId))
    }

    // Handle effects
    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MatchDetailEffect.NavigateBack -> {
                    onNavigateBack()
                }
                else -> {
                    // Handle other effects
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Match Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.NavigateBack) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.ShareMatch) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.ToggleFavorite) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
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
                        .background(MaterialTheme.colorScheme.background),
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
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorState(
                        message = matchState.message,
                        onRetry = {
                            viewModel.handleAction(MatchDetailAction.RefreshMatch)
                        }
                    )
                }
            }

            is UiState.Success -> {
                val match = matchState.data
                ModernMatchDetailContent(
                    match = match,
                    state = state,
                    onAction = viewModel::handleAction,
                    onNavigateToTeamDetail = onNavigateToTeamDetail,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ModernMatchDetailContent(
    match: Match,
    state: MatchDetailState,
    onAction: (MatchDetailAction) -> Unit,
    onNavigateToTeamDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Match Header Section
        MatchHeaderSection(
            match = match,
            onNavigateToTeamDetail = onNavigateToTeamDetail
        )

        // Tab Navigation
        ModernTabRow(
            selectedTab = state.selectedTab,
            onTabSelected = { tab ->
                onAction(MatchDetailAction.SelectTab(tab))
            }
        )

        // Banner Ad
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BannerAdManager.BannerAdView()
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            when (state.selectedTab) {
                MatchDetailTab.OVERVIEW -> {
                    ModernOverviewTab(match, state)
                }
                MatchDetailTab.FIXTURES -> {
                    ModernFixturesTab(match, state)
                }
                MatchDetailTab.LINEUPS -> {
                    ModernLineupsTab(match, state)
                }
                MatchDetailTab.STATISTICS -> {
                    ModernStatsTab(match, state)
                }
                MatchDetailTab.STANDINGS -> {
                    ModernStandingsTab(match, state)
                }
                MatchDetailTab.HEAD_TO_HEAD -> {
                    ModernHeadToHeadTab(match, state)
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Coming Soon",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchHeaderSection(
    match: Match,
    onNavigateToTeamDetail: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // League Info
            Text(
                text = "${match.league.country} • ${match.league.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teams and Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    TeamRow(
                        teamName = match.homeTeam.name,
                        logoUrl = match.homeTeam.logo,
                        isHome = true,
                        compact = false,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNavigateToTeamDetail(match.homeTeam.id) }
                    )
                }

                // Score/Time
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    if (match.hasScore()) {
                        Text(
                            text = "${match.score.home ?: 0} - ${match.score.away ?: 0}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        )
                    } else {
                        Text(
                            text = match.getTimeDisplay(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Text(
                        text = match.status.long,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (match.status.isLive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                }

                // Away Team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    TeamRow(
                        teamName = match.awayTeam.name,
                        logoUrl = match.awayTeam.logo,
                        isHome = false,
                        compact = false,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNavigateToTeamDetail(match.awayTeam.id) }
                    )
                }
            }

            // Match Status Additional Info
            match.status.elapsed?.let { elapsed ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${elapsed}'",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernTabRow(
    selectedTab: MatchDetailTab,
    onTabSelected: (MatchDetailTab) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { },
        divider = {}
    ) {
        MatchDetailTab.values().forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.title,
                        color = if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }
    }
}

@Composable
private fun ModernOverviewTab(match: Match, state: MatchDetailState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Match Timeline - Chronological Events (Priority Display)
        when (val eventsState = state.events) {
            is UiState.Success -> {
                if (eventsState.data.isNotEmpty()) {
                    item {
                        MatchTimelineCard(events = eventsState.data, match = match)
                    }
                }
            }
            else -> { /* Loading or Error states handled in events tab */ }
        }

        // Match Information Card
        item {
            ModernInfoCard(
                title = "Match Information",
                items = buildList {
                    match.fixture.dateTime?.let {
                        add("Kick-off" to it.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")))
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

        // Goals/Score Information
        if (match.hasScore()) {
            item {
                ModernInfoCard(
                    title = "Score Details",
                    items = listOf(
                        "Halftime" to "${match.score.halftime?.home ?: 0} - ${match.score.halftime?.away ?: 0}",
                        "Fulltime" to "${match.score.fulltime?.home ?: 0} - ${match.score.fulltime?.away ?: 0}",
                        "Extra Time" to "${match.score.extratime?.home ?: 0} - ${match.score.extratime?.away ?: 0}",
                        "Penalties" to "${match.score.penalties?.home ?: 0} - ${match.score.penalties?.away ?: 0}"
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernFixturesTab(match: Match, state: MatchDetailState) {
    when (val fixturesState = state.fixtures) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error loading fixtures",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = fixturesState.message,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success -> {
            val fixturesData = fixturesState.data
            if (fixturesData.homeTeamFixtures.isEmpty() && fixturesData.awayTeamFixtures.isEmpty()) {
                EmptyFixturesMessage()
            } else {
                FixturesList(fixturesData = fixturesData, match = match)
            }
        }
    }
}

@Composable
private fun EmptyFixturesMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Fixtures Available",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "League fixtures will appear here when available",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FixturesList(fixturesData: FixturesData, match: Match) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (fixturesData.homeTeamFixtures.isNotEmpty()) {
            item {
                Text(
                    text = "${match.homeTeam.name} Fixtures",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
            }
            items(fixturesData.homeTeamFixtures) { fixture ->
                FixtureCard(fixture = fixture)
            }
        }

        if (fixturesData.awayTeamFixtures.isNotEmpty()) {
            item {
                Text(
                    text = "${match.awayTeam.name} Fixtures",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
            items(fixturesData.awayTeamFixtures) { fixture ->
                FixtureCard(fixture = fixture)
            }
        }
    }
}

@Composable
private fun FixtureCard(fixture: Match) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Date and time
            Text(
                text = fixture.fixture.dateTime?.format(
                    java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm")
                ) ?: "TBD",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Teams and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = fixture.homeTeam.logo,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        placeholder = painterResource(android.R.drawable.ic_menu_gallery)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = fixture.homeTeam.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Score or status
                Text(
                    text = if (fixture.status.isFinished) {
                        "${fixture.score.home ?: 0} - ${fixture.score.away ?: 0}"
                    } else {
                        fixture.status.short
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Away team
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = fixture.awayTeam.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = fixture.awayTeam.logo,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        placeholder = painterResource(android.R.drawable.ic_menu_gallery)
                    )
                }
            }

            // Round info
            if (!fixture.league.round.isNullOrEmpty()) {
                Text(
                    text = fixture.league.round,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ModernLineupsTab(match: Match, state: MatchDetailState) {
    when (val lineupsState = state.lineups) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error loading lineups",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lineupsState.message,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success -> {
            val lineups = lineupsState.data
            if (lineups.isEmpty()) {
                EmptyLineupsMessage()
            } else {
                LineupsContent(lineups = lineups, match = match)
            }
        }
    }
}

@Composable
private fun EmptyLineupsMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lineups Not Available",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Team lineups will be available closer to kickoff",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LineupsContent(lineups: List<Lineup>, match: Match) {
    var viewMode by remember { mutableStateOf("formation") } // "formation" or "list"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // View Mode Toggle
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    onClick = { viewMode = "formation" },
                    label = { Text("Formation View") },
                    selected = viewMode == "formation",
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    onClick = { viewMode = "list" },
                    label = { Text("List View") },
                    selected = viewMode == "list"
                )
            }
        }

        if (viewMode == "formation") {
            items(lineups) { lineup ->
                FormationView(
                    lineup = lineup,
                    isHomeTeam = lineup.team.id == match.homeTeam.id
                )
            }
        } else {
            items(lineups) { lineup ->
                TeamLineupCard(
                    lineup = lineup,
                    isHomeTeam = lineup.team.id == match.homeTeam.id
                )
            }
        }
    }
}

@Composable
private fun TeamLineupCard(lineup: Lineup, isHomeTeam: Boolean) {
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
            // Header with team name and formation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = lineup.team.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Formation: ${lineup.formation}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Text(
                    text = if (isHomeTeam) "HOME" else "AWAY",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFFFB000),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(
                            color = Color(0xFFFFB000).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coach
            Text(
                text = "Coach: ${lineup.coach.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Starting XI
            Text(
                text = "Starting XI",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            lineup.startingEleven.forEach { lineupPlayer ->
                PlayerRow(
                    player = lineupPlayer,
                    isStarter = true
                )
            }

            if (lineup.substitutes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Substitutes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFFFFB000),
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                lineup.substitutes.forEach { lineupPlayer ->
                    PlayerRow(
                        player = lineupPlayer,
                        isStarter = false
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerRow(player: LineupPlayer, isStarter: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Player number
        Text(
            text = player.player.number?.toString() ?: "-",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFFFFB000),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .background(
                    color = if (isStarter) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .width(32.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Player name and position
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = player.player.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (isStarter) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isStarter) FontWeight.Medium else FontWeight.Normal
                )
            )
            Text(
                text = player.position,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.outline
                )
            )
        }

        // Grid position (if available)
        player.grid?.let { grid ->
            Text(
                text = grid,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Composable
private fun ModernStatsTab(match: Match, state: MatchDetailState) {
    when (val statsState = state.statistics) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error loading statistics",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = statsState.message,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success -> {
            val statistics = statsState.data
            if (statistics.isEmpty()) {
                EmptyStatsMessage()
            } else {
                StatisticsContent(statistics = statistics, match = match)
            }
        }
    }
}

@Composable
private fun EmptyStatsMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Statistics Not Available",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Match statistics will be available during and after the match",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatisticsContent(statistics: List<MatchStatistic>, match: Match) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
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
                        text = "Match Statistics",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Team headers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = match.homeTeam.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = match.awayTeam.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    statistics.forEach { stat ->
                        StatisticRow(statistic = stat)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticRow(statistic: MatchStatistic) {
    Column {
        // Statistic name
        Text(
            text = statistic.type,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Values and bars
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home value
            Text(
                text = statistic.homeValue ?: "0",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(50.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Progress bars
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
            ) {
                val homePercentage = statistic.homePercentage ?: 0f
                val awayPercentage = statistic.awayPercentage ?: 0f
                val total = homePercentage + awayPercentage

                if (total > 0) {
                    val homeWidth = (homePercentage / total).coerceAtLeast(0.01f)
                    val awayWidth = (awayPercentage / total).coerceAtLeast(0.01f)

                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Home team bar
                        Box(
                            modifier = Modifier
                                .weight(homeWidth)
                                .fillMaxHeight()
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(
                                        topStart = 12.dp,
                                        bottomStart = 12.dp
                                    )
                                )
                        )

                        // Away team bar
                        Box(
                            modifier = Modifier
                                .weight(awayWidth)
                                .fillMaxHeight()
                                .background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(
                                        topEnd = 12.dp,
                                        bottomEnd = 12.dp
                                    )
                                )
                        )
                    }
                } else {
                    // Fallback for non-percentage values
                    val homeValue = parseStatValue(statistic.homeValue)
                    val awayValue = parseStatValue(statistic.awayValue)
                    val total = homeValue + awayValue

                    if (total > 0) {
                        val homeWidth = (homeValue / total).coerceAtLeast(0.01f)
                        val awayWidth = (awayValue / total).coerceAtLeast(0.01f)

                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(homeWidth)
                                    .fillMaxHeight()
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            bottomStart = 12.dp
                                        )
                                    )
                            )

                            Box(
                                modifier = Modifier
                                    .weight(awayWidth)
                                    .fillMaxHeight()
                                    .background(
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = RoundedCornerShape(
                                            topEnd = 12.dp,
                                            bottomEnd = 12.dp
                                        )
                                    )
                            )
                        }
                    } else {
                        // Empty state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Away value
            Text(
                text = statistic.awayValue ?: "0",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(50.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun parseStatValue(value: String?): Float {
    return try {
        value?.replace("%", "")?.toFloat() ?: 0f
    } catch (e: NumberFormatException) {
        0f
    }
}

@Composable
private fun ModernHeadToHeadTab(match: Match, state: MatchDetailState) {
    when (val h2hState = state.headToHead) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error loading head-to-head",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = h2hState.message,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success -> {
            val h2hMatches = h2hState.data
            if (h2hMatches.isEmpty()) {
                EmptyHeadToHeadMessage(match)
            } else {
                HeadToHeadContent(matches = h2hMatches, currentMatch = match)
            }
        }
    }
}

@Composable
private fun EmptyHeadToHeadMessage(match: Match) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Previous Matches",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No recent matches found between ${match.homeTeam.name} and ${match.awayTeam.name}",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HeadToHeadContent(matches: List<Match>, currentMatch: Match) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            // Summary card
            HeadToHeadSummaryCard(matches = matches, currentMatch = currentMatch)
        }

        item {
            val isDark = isSystemInDarkTheme()
            Text(
                text = "Recent Matches",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (isDark) Color(0xFFFFB000) else Color(0xFFD97706),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(matches.take(10)) { match ->
            HeadToHeadMatchCard(match = match, currentMatch = currentMatch)
        }
    }
}

@Composable
private fun HeadToHeadSummaryCard(matches: List<Match>, currentMatch: Match) {
    val homeTeamWins = matches.count { match ->
        val winner = match.getWinner()
        winner?.id == currentMatch.homeTeam.id
    }
    val awayTeamWins = matches.count { match ->
        val winner = match.getWinner()
        winner?.id == currentMatch.awayTeam.id
    }
    val draws = matches.count { it.isDraw() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Head-to-Head Record",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "Last ${matches.size} matches",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RecordColumn(
                    title = currentMatch.homeTeam.name,
                    wins = homeTeamWins,
                    totalMatches = matches.size
                )

                RecordColumn(
                    title = "Draws",
                    wins = draws,
                    totalMatches = matches.size
                )

                RecordColumn(
                    title = currentMatch.awayTeam.name,
                    wins = awayTeamWins,
                    totalMatches = matches.size
                )
            }
        }
    }
}

@Composable
private fun RecordColumn(
    title: String,
    wins: Int,
    totalMatches: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = wins.toString(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = if (totalMatches > 0) "${(wins * 100 / totalMatches)}%" else "0%",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun HeadToHeadMatchCard(match: Match, currentMatch: Match) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Date and competition
            Text(
                text = "${match.league.name} • ${match.fixture.dateTime.toLocalDate()}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Teams and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.homeTeam.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.weight(1f)
                )

                if (match.hasScore()) {
                    Text(
                        text = match.getScoreDisplay(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color(0xFFFFB000),
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    Text(
                        text = match.getTimeDisplay(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Text(
                    text = match.awayTeam.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            // Winner indicator
            match.getWinner()?.let { winner ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Winner: ${winner.name}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF10B981)
                    )
                )
            }

            if (match.isDraw()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Draw",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFF59E0B)
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernInfoCard(
    title: String,
    items: List<Pair<String, String>>
) {
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
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernStandingsTab(match: Match, state: MatchDetailState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "View League Standings",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Check the complete ${match.league.name} standings in the Leagues section",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // TODO: Navigate to leagues page
                // This would typically use a navigation callback
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go to Leagues")
        }
    }
}

@Composable
private fun EmptyStandingsMessage(match: Match) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Standings Not Available",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "League standings for ${match.league.name} are not available",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StandingsContent(standings: List<Standing>, match: Match) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "${match.league.name} Standings",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            StandingsHeader()
        }

        items(standings) { standing ->
            StandingRow(
                standing = standing,
                isHighlighted = standing.team.id == match.homeTeam.id || standing.team.id == match.awayTeam.id
            )
        }
    }
}

@Composable
private fun StandingsHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pos",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(40.dp)
            )
            Text(
                text = "Team",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "MP",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "W",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "D",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "L",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Pts",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StandingRow(standing: Standing, isHighlighted: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isHighlighted) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = standing.rank.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamRow(
                    teamName = standing.team.name,
                    logoUrl = standing.team.logo,
                    isHome = true,
                    compact = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = standing.all.played.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.all.win.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.all.draw.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.all.lose.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.points.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun MatchTimelineCard(events: List<MatchEvent>, match: Match) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Simple header
        Text(
            text = "Match Timeline",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sort all events chronologically
        val chronologicalEvents = events.sortedBy { it.time.elapsed }

        if (chronologicalEvents.isEmpty()) {
            Text(
                text = "No events to display",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(vertical = 32.dp)
            )
        } else {
            chronologicalEvents.forEach { event ->
                SimpleEventRow(event = event, match = match)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
private fun SimpleEventRow(event: MatchEvent, match: Match) {
    val isHomeTeam = event.team.id == match.homeTeam.id

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isHomeTeam) {
            // Home team events (left side)
            Box(modifier = Modifier.weight(0.4f)) {
                HomeTeamEvent(event)
            }
            Box(modifier = Modifier.weight(0.2f)) {
                CenterTimeline(event)
            }
            Spacer(modifier = Modifier.weight(0.4f))
        } else {
            // Away team events (right side)
            Spacer(modifier = Modifier.weight(0.4f))
            Box(modifier = Modifier.weight(0.2f)) {
                CenterTimeline(event)
            }
            Box(modifier = Modifier.weight(0.4f)) {
                AwayTeamEvent(event)
            }
        }
    }
}

@Composable
private fun HomeTeamEvent(event: MatchEvent) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Event details (left aligned)
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = event.player.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Start
            )

            // Show substitution details
            if (event.type == EventType.SUBSTITUTION && !event.detail.isNullOrBlank()) {
                Text(
                    text = "↑ In: ${event.player.name}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "↓ Out: ${event.detail}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Start
                )
            }

            // Show assist for goals
            if ((event.type == EventType.GOAL || event.type == EventType.PENALTY) && event.assist != null) {
                Text(
                    text = "Assist: ${event.assist.name}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Event icon
        Text(
            text = event.type.iconCode,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun AwayTeamEvent(event: MatchEvent) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Event icon
        Text(
            text = event.type.iconCode,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Event details (right aligned)
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = event.player.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.End
            )

            // Show substitution details
            if (event.type == EventType.SUBSTITUTION && !event.detail.isNullOrBlank()) {
                Text(
                    text = "In: ${event.player.name} ↑",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.End
                )
                Text(
                    text = "Out: ${event.detail} ↓",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.End
                )
            }

            // Show assist for goals
            if ((event.type == EventType.GOAL || event.type == EventType.PENALTY) && event.assist != null) {
                Text(
                    text = "Assist: ${event.assist.name}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun CenterTimeline(event: MatchEvent) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Time
        Text(
            text = "${event.time.elapsed}'${event.time.extra?.let { "+$it" } ?: ""}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        // Optional: Add a small divider line to show timeline
        Box(
            modifier = Modifier
                .height(1.dp)
                .width(30.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        )
    }
}




@Composable
private fun FormationView(lineup: Lineup, isHomeTeam: Boolean) {
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
            // Header with team name and formation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = lineup.team.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Formation: ${lineup.formation}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Text(
                    text = if (isHomeTeam) "HOME" else "AWAY",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Football pitch with players
            FootballPitch(
                players = lineup.startingEleven,
                formation = lineup.formation,
                isHomeTeam = isHomeTeam
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Coach
            Text(
                text = "Coach: ${lineup.coach.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            if (lineup.substitutes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Substitutes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Substitutes in a horizontal scrollable row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(lineup.substitutes) { substitute ->
                        SubstitutePlayerCard(player = substitute)
                    }
                }
            }
        }
    }
}

@Composable
private fun FootballPitch(
    players: List<LineupPlayer>,
    formation: String,
    isHomeTeam: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                color = Color(0xFF2E7D32).copy(alpha = 0.8f), // Football pitch green
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        // Draw pitch lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2.dp.toPx()
            val centerY = size.height / 2
            val centerX = size.width / 2

            // Center line
            drawLine(
                color = Color.White,
                start = Offset(centerX, 0f),
                end = Offset(centerX, size.height),
                strokeWidth = strokeWidth
            )

            // Center circle
            drawCircle(
                color = Color.White,
                radius = 30.dp.toPx(),
                center = Offset(centerX, centerY),
                style = Stroke(width = strokeWidth)
            )

            // Goal areas
            val goalWidth = 60.dp.toPx()
            val goalHeight = 20.dp.toPx()

            // Home goal (left)
            drawRect(
                color = Color.White,
                topLeft = Offset(0f, centerY - goalHeight / 2),
                size = Size(goalWidth, goalHeight),
                style = Stroke(width = strokeWidth)
            )

            // Away goal (right)
            drawRect(
                color = Color.White,
                topLeft = Offset(size.width - goalWidth, centerY - goalHeight / 2),
                size = Size(goalWidth, goalHeight),
                style = Stroke(width = strokeWidth)
            )
        }

        // Position players based on formation using BoxWithConstraints for responsive positioning
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val positions = getFormationPositions(formation, isHomeTeam)
            val pitchWidth = maxWidth.value
            val pitchHeight = maxHeight.value

            players.forEachIndexed { index, player ->
                if (index < positions.size) {
                    val position = positions[index]
                    // Convert relative positions (0-1) to actual coordinates
                    val xPos = (position.first * pitchWidth).dp
                    val yPos = (position.second * pitchHeight).dp

                    PlayerDot(
                        player = player,
                        modifier = Modifier
                            .offset(x = xPos, y = yPos)
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerDot(
    player: LineupPlayer,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .border(
                    width = 1.5.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.player.number?.toString() ?: "?",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = player.player.name.split(" ").lastOrNull() ?: player.player.name,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Medium
            ),
            fontSize = 9.sp,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            modifier = Modifier.width(45.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SubstitutePlayerCard(player: LineupPlayer) {
    Card(
        modifier = Modifier.width(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = player.player.number?.toString() ?: "?",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = player.player.name.split(" ").lastOrNull() ?: player.player.name,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                fontSize = 10.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Text(
                text = player.position,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                fontSize = 8.sp,
                maxLines = 1
            )
        }
    }
}

private fun getFormationPositions(formation: String, isHomeTeam: Boolean): List<Pair<Float, Float>> {
    val basePositions = when (formation) {
        "4-4-2" -> listOf(
            // Goalkeeper
            0.05f to 0.5f,
            // Defense (4)
            0.25f to 0.2f, 0.25f to 0.4f, 0.25f to 0.6f, 0.25f to 0.8f,
            // Midfield (4)
            0.55f to 0.2f, 0.55f to 0.4f, 0.55f to 0.6f, 0.55f to 0.8f,
            // Attack (2)
            0.8f to 0.35f, 0.8f to 0.65f
        )
        "4-3-3" -> listOf(
            // Goalkeeper
            0.05f to 0.5f,
            // Defense (4)
            0.25f to 0.2f, 0.25f to 0.4f, 0.25f to 0.6f, 0.25f to 0.8f,
            // Midfield (3)
            0.55f to 0.3f, 0.55f to 0.5f, 0.55f to 0.7f,
            // Attack (3)
            0.8f to 0.25f, 0.8f to 0.5f, 0.8f to 0.75f
        )
        "3-5-2" -> listOf(
            // Goalkeeper
            0.05f to 0.5f,
            // Defense (3)
            0.25f to 0.3f, 0.25f to 0.5f, 0.25f to 0.7f,
            // Midfield (5)
            0.55f to 0.15f, 0.55f to 0.35f, 0.55f to 0.5f, 0.55f to 0.65f, 0.55f to 0.85f,
            // Attack (2)
            0.8f to 0.35f, 0.8f to 0.65f
        )
        "5-3-2" -> listOf(
            // Goalkeeper
            0.05f to 0.5f,
            // Defense (5)
            0.25f to 0.1f, 0.25f to 0.3f, 0.25f to 0.5f, 0.25f to 0.7f, 0.25f to 0.9f,
            // Midfield (3)
            0.55f to 0.3f, 0.55f to 0.5f, 0.55f to 0.7f,
            // Attack (2)
            0.8f to 0.35f, 0.8f to 0.65f
        )
        else -> listOf(
            // Default 4-4-2
            0.05f to 0.5f,
            0.25f to 0.2f, 0.25f to 0.4f, 0.25f to 0.6f, 0.25f to 0.8f,
            0.55f to 0.2f, 0.55f to 0.4f, 0.55f to 0.6f, 0.55f to 0.8f,
            0.8f to 0.35f, 0.8f to 0.65f
        )
    }

    return if (isHomeTeam) {
        basePositions
    } else {
        // Mirror positions for away team (flip horizontally)
        basePositions.map { (x, y) -> (1f - x) to y }
    }
}