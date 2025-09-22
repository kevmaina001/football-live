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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.score24seven.ui.components.ErrorState
import com.score24seven.ui.components.LoadingState
import com.score24seven.ui.design.components.ScoreChip
import com.score24seven.ui.design.components.TeamRow
import com.score24seven.ui.design.tokens.Score24SevenTypography
import com.score24seven.ui.design.tokens.Spacing
import com.score24seven.ui.state.*
import com.score24seven.ui.viewmodel.MatchDetailViewModel
import com.score24seven.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMatchDetailScreen(
    matchId: Int,
    onNavigateBack: () -> Unit,
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
        containerColor = Color(0xFF0A1628), // Dark blue background
        topBar = {
            TopAppBar(
                title = {
                    when (val matchState = state.match) {
                        is UiState.Success -> {
                            Text(
                                text = "Match Details",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        else -> {
                            Text(
                                "Match Details",
                                color = Color.White
                            )
                        }
                    }
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
                    IconButton(
                        onClick = { viewModel.handleAction(MatchDetailAction.ToggleFavorite) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A1628),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628))
    ) {
        // Match Header Section
        MatchHeaderSection(match = match)

        // Tab Navigation
        ModernTabRow(
            selectedTab = state.selectedTab,
            onTabSelected = { tab ->
                onAction(MatchDetailAction.SelectTab(tab))
            }
        )

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A1628))
                .padding(16.dp)
        ) {
            when (state.selectedTab) {
                MatchDetailTab.OVERVIEW -> {
                    ModernOverviewTab(match)
                }
                MatchDetailTab.EVENTS -> {
                    ModernEventsTab(match)
                }
                MatchDetailTab.LINEUPS -> {
                    ModernLineupsTab(match)
                }
                MatchDetailTab.STATISTICS -> {
                    ModernStatsTab(match)
                }
                MatchDetailTab.HEAD_TO_HEAD -> {
                    ModernHeadToHeadTab(match)
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Coming Soon",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchHeaderSection(match: Match) {
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
            // League Info
            Text(
                text = "${match.league.country} • ${match.league.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF),
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
                        modifier = Modifier.fillMaxWidth()
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Match Status Additional Info
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
private fun ModernTabRow(
    selectedTab: MatchDetailTab,
    onTabSelected: (MatchDetailTab) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTab.ordinal,
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
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.title,
                        color = if (selectedTab == tab) Color(0xFFFFB000) else Color(0xFF9CA3AF),
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }
    }
}

@Composable
private fun ModernOverviewTab(match: Match) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Match Information Card
        item {
            ModernInfoCard(
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

        // Goals/Score Information
        if (match.hasScore()) {
            item {
                ModernInfoCard(
                    title = "Score Details",
                    items = listOf(
                        "Halftime" to "${match.score.halftime?.home ?: 0} - ${match.score.halftime?.away ?: 0}",
                        "Fulltime" to "${match.score.fulltime?.home ?: 0} - ${match.score.fulltime?.away ?: 0}",
                        "Extra Time" to "${match.score.extratime?.home ?: 0} - ${match.score.extratime?.away ?: 0}",
                        "Penalties" to "0 - 0"
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernEventsTab(match: Match) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Match Events",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Live events and match timeline will appear here during the match",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ModernLineupsTab(match: Match) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Team Lineups",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Starting lineups and formations will be displayed here",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ModernStatsTab(match: Match) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Match Statistics",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Possession, shots, passes and other match statistics",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ModernHeadToHeadTab(match: Match) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Head to Head",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Previous matches between ${match.homeTeam.name} and ${match.awayTeam.name}",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center
        )
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
            containerColor = Color(0xFF1A2332)
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