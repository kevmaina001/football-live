/*
 * Competition Matches Screen - Shows matches for a specific competition
 */

package com.kickscore.live.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Match
import com.kickscore.live.ui.components.ErrorState
import com.kickscore.live.ui.components.LoadingState
import com.kickscore.live.ui.components.EmptyState
import com.kickscore.live.ui.components.ModernMatchCard
import com.kickscore.live.ui.design.tokens.Spacing
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.ui.viewmodel.MatchesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionMatchesScreen(
    competitionId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToMatchDetail: (Match) -> Unit,
    viewModel: MatchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dates = remember { generateDatesList() }
    var selectedDateIndex by remember { mutableIntStateOf(2) } // Default to today

    LaunchedEffect(selectedDateIndex) {
        val selectedDate = dates[selectedDateIndex]
        viewModel.loadMatchesForDate(selectedDate)
    }

    Column(
        modifier = Modifier
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
        // Competition Header
        CompetitionHeader(
            competition = when (val matches = state.matches) {
                is UiState.Success -> getCompetitionFromMatches(matches.data, competitionId)
                else -> null
            },
            onNavigateBack = onNavigateBack
        )

        // Date Tabs
        DateTabRow(
            dates = dates,
            selectedDateIndex = selectedDateIndex,
            onDateSelected = { selectedDateIndex = it }
        )

        // Competition Matches Content
        AnimatedContent(
            targetState = selectedDateIndex,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier.fillMaxSize(),
            label = "competition_matches_content"
        ) { dateIndex ->
            CompetitionMatchesContent(
                matches = state.matches,
                competitionId = competitionId,
                selectedDate = dates[dateIndex],
                onMatchClick = onNavigateToMatchDetail,
                onLiveClick = { /* TODO: Handle live click */ },
                onFavoriteClick = { /* TODO: Handle favorite click */ }
            )
        }
    }
}

@Composable
private fun CompetitionHeader(
    competition: League?,
    onNavigateBack: () -> Unit
) {
    val (countryFlag, leagueBadge) = if (competition != null) {
        getCompetitionDisplayInfo(competition)
    } else {
        "🌍" to "⚽"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.lg),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(12.dp)
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(Spacing.md))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = countryFlag,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = leagueBadge,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = competition?.name ?: "Competition",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = competition?.let { getCompetitionSubtitle(it) }
                            ?: "Competition matches",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DateTabRow(
    dates: List<LocalDate>,
    selectedDateIndex: Int,
    onDateSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedDateIndex,
        modifier = Modifier.padding(horizontal = Spacing.lg),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            if (selectedDateIndex < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedDateIndex])
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        dates.forEachIndexed { index, date ->
            Tab(
                selected = selectedDateIndex == index,
                onClick = { onDateSelected(index) },
                modifier = Modifier.padding(vertical = Spacing.sm)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(Spacing.sm)
                ) {
                    Text(
                        text = when (index) {
                            1 -> "Yesterday"
                            2 -> "Today"
                            3 -> "Tomorrow"
                            else -> date.format(DateTimeFormatter.ofPattern("EEE"))
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selectedDateIndex == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedDateIndex == index) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("dd MMM")),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selectedDateIndex == index) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CompetitionMatchesContent(
    matches: UiState<List<Match>>,
    competitionId: Int,
    selectedDate: LocalDate,
    onMatchClick: (Match) -> Unit,
    onLiveClick: (Match) -> Unit,
    onFavoriteClick: (Match) -> Unit
) {
    when (matches) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingState()
            }
        }

        is UiState.Success -> {
            // Filter matches for this specific competition
            val competitionMatches = matches.data.filter { it.league.id == competitionId }

            if (competitionMatches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        message = "No matches in this competition for ${
                            selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                        }"
                    )
                }
            } else {
                val sortedMatches = competitionMatches.sortedBy { it.fixture.timestamp }
                val listState = rememberLazyListState()

                // Auto-scroll to current/next match
                LaunchedEffect(sortedMatches) {
                    val currentTime = System.currentTimeMillis()
                    val currentMatchIndex = sortedMatches.indexOfFirst { match ->
                        match.fixture.timestamp * 1000 > currentTime
                    }
                    if (currentMatchIndex > 0) {
                        listState.scrollToItem(maxOf(0, currentMatchIndex - 1))
                    }
                }

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xs),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    items(sortedMatches) { match ->
                        ModernMatchCard(
                            match = match,
                            onClick = { onMatchClick(match) },
                            onLiveClick = { onLiveClick(match) },
                            onFavoriteClick = { onFavoriteClick(match) }
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorState(
                    message = matches.message,
                    onRetry = null
                )
            }
        }
    }
}

// Helper functions
private fun generateDatesList(): List<LocalDate> {
    val today = LocalDate.now()
    return listOf(
        today.minusDays(2), // 2 days ago
        today.minusDays(1), // Yesterday
        today,              // Today
        today.plusDays(1),  // Tomorrow
        today.plusDays(2),  // 2 days later
        today.plusDays(3),  // 3 days later
        today.plusDays(4)   // 4 days later
    )
}

private fun getCompetitionFromMatches(matches: List<Match>?, competitionId: Int): League? {
    return matches?.find { it.league.id == competitionId }?.league
}

private fun getCompetitionDisplayInfo(league: League): Pair<String, String> {
    return when (league.id) {
        39 -> "🏴󠁧󠁢󠁥󠁮󠁧󠁿" to "👑"   // English Premier League
        140 -> "🇪🇸" to "🏆"              // La Liga (Spain)
        78 -> "🇩🇪" to "🦅"               // Bundesliga (Germany)
        135 -> "🇮🇹" to "⭐"              // Serie A (Italy)
        61 -> "🇫🇷" to "🐓"               // Ligue 1 (France)
        2 -> "🇪🇺" to "🏆"                // UEFA Champions League
        3 -> "🇪🇺" to "🥈"                // UEFA Europa League
        848 -> "🇪🇺" to "🥉"              // UEFA Conference League
        else -> getCountryFlag(league.country) to "⚽"
    }
}

private fun getCountryFlag(country: String): String {
    return when (country.trim().lowercase()) {
        "spain" -> "🇪🇸"
        "germany" -> "🇩🇪"
        "france" -> "🇫🇷"
        "italy" -> "🇮🇹"
        "portugal" -> "🇵🇹"
        "netherlands" -> "🇳🇱"
        "england", "united kingdom" -> "🏴󠁧󠁢󠁥󠁮󠁧󠁿"
        else -> "🌍"
    }
}

private fun getCompetitionSubtitle(league: League): String {
    return when (league.id) {
        39 -> "England • Premier Division"
        140 -> "Spain • Primera División"
        78 -> "Germany • 1. Bundesliga"
        135 -> "Italy • Serie A TIM"
        61 -> "France • Ligue 1 Uber Eats"
        2 -> "Europe • Champions League"
        3 -> "Europe • Europa League"
        848 -> "Europe • Conference League"
        else -> "${league.country} • ${league.name}"
    }
}