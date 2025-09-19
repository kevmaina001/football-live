/*
 * Competitions Screen - Browse available football competitions
 */

package com.kickscore.live.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Match
import com.kickscore.live.ui.components.ErrorState
import com.kickscore.live.ui.components.LoadingState
import com.kickscore.live.ui.components.EmptyState
import com.kickscore.live.ui.design.tokens.Spacing
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.ui.viewmodel.MatchesViewModel

data class CompetitionInfo(
    val league: League,
    val totalMatches: Int,
    val liveMatches: Int,
    val todayMatches: Int,
    val upcomingMatches: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCompetition: (Int) -> Unit,
    viewModel: MatchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        // Load today's matches to analyze available competitions
        viewModel.loadMatchesForDate(java.time.LocalDate.now())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628)) // Dark blue background like the reference
    ) {
        // Header
        CompetitionsHeader(onNavigateBack = onNavigateBack)

        // Competitions Content
        val matchesState = state.matches
        when (matchesState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState(message = "Loading competitions...")
                }
            }
            is UiState.Success -> {
                val competitions = analyzeCompetitions(matchesState.data)

                if (competitions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyState(message = "No competitions available")
                    }
                } else {
                    CompetitionsList(
                        competitions = competitions,
                        onCompetitionClick = onNavigateToCompetition
                    )
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorState(
                        message = matchesState.message,
                        onRetry = {
                            viewModel.loadMatchesForDate(java.time.LocalDate.now())
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CompetitionsHeader(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Top Bar with back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "⚽ Football",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            // Search and profile icons like in reference
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight, // Placeholder for search
                    contentDescription = "Search",
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Date tabs like in reference
        DateTabsLikeReference()

        Spacer(modifier = Modifier.height(20.dp))

        // All games header with count
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight, // Placeholder for menu icon
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "All competitions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            // Competition count badge
            Box(
                modifier = Modifier
                    .background(Color(0xFFDC143C), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "15", // Placeholder
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DateTabsLikeReference() {
    val dates = remember {
        listOf("THU\n11.09.", "FRI\n12.09.", "SAT\n13.09.", "TODAY\n14.09.", "MON\n15.09.", "TUE\n16.09.", "WED\n17.09.")
    }
    var selectedIndex by remember { mutableIntStateOf(3) } // TODAY selected

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(dates) { index, date ->
            Column(
                modifier = Modifier
                    .clickable { selectedIndex = index }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selectedIndex == index) Color.White else Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
                if (selectedIndex == index) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(3.dp)
                            .background(Color(0xFFDC143C), RoundedCornerShape(2.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun CompetitionsList(
    competitions: List<CompetitionInfo>,
    onCompetitionClick: (Int) -> Unit
) {
    // Separate priority and other competitions
    val priorityCompetitions = competitions.filter { isPriorityLeague(it.league.id) }
    val otherCompetitions = competitions.filter { !isPriorityLeague(it.league.id) }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp) // Very tight spacing like reference
    ) {
        // Favourite Competitions Section
        if (priorityCompetitions.isNotEmpty()) {
            item {
                Text(
                    text = "FAVOURITE COMPETITIONS",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFFB000), // Golden yellow like reference
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            items(priorityCompetitions) { competition ->
                ModernCompetitionCard(
                    competition = competition,
                    onClick = { onCompetitionClick(competition.league.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Other Competitions Section
        if (otherCompetitions.isNotEmpty()) {
            item {
                Text(
                    text = "OTHER COMPETITIONS [A-Z]",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFFB000), // Golden yellow like reference
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            items(otherCompetitions) { competition ->
                ModernCompetitionCard(
                    competition = competition,
                    onClick = { onCompetitionClick(competition.league.id) }
                )
            }
        }
    }
}

@Composable
private fun ModernCompetitionCard(
    competition: CompetitionInfo,
    onClick: () -> Unit
) {
    val (countryFlag, _) = getCompetitionDisplayInfo(competition.league)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Country flag
        Text(
            text = countryFlag,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Competition info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = getCountryName(competition.league.country).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = competition.league.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }

        // Live indicator and count
        if (competition.liveMatches > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Live indicator (headphones icon placeholder)
                Text(
                    text = "🎧",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Live count
                Box(
                    modifier = Modifier
                        .background(Color(0xFFDC143C), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = competition.liveMatches.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        // Total matches count
        Text(
            text = competition.totalMatches.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CompetitionCard(
    competition: CompetitionInfo,
    onClick: () -> Unit
) {
    // Keeping old implementation for fallback
    ModernCompetitionCard(competition, onClick)
}


// Analyze matches to create competition information
private fun analyzeCompetitions(matches: List<Match>): List<CompetitionInfo> {
    val matchesByLeague = matches.groupBy { it.league }

    return matchesByLeague.map { (league, leagueMatches) ->
        CompetitionInfo(
            league = league,
            totalMatches = leagueMatches.size,
            liveMatches = leagueMatches.count { it.status.isLive },
            todayMatches = leagueMatches.count { match ->
                val today = java.time.LocalDate.now()
                match.fixture.dateTime.toLocalDate() == today
            },
            upcomingMatches = leagueMatches.count { match ->
                match.fixture.dateTime.isAfter(java.time.LocalDateTime.now())
            }
        )
    }.sortedWith(
        compareByDescending<CompetitionInfo> { isPriorityLeague(it.league.id) }
        .thenByDescending { it.liveMatches }
        .thenBy { it.league.country }
        .thenBy { it.league.name }
    )
}

private fun isPriorityLeague(leagueId: Int): Boolean {
    val priorityLeagueIds = intArrayOf(39, 140, 78, 135, 61, 2, 3, 848)
    return leagueId in priorityLeagueIds
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

private fun getCountryName(country: String): String {
    return when (country.trim().lowercase()) {
        "spain" -> "Spain"
        "germany" -> "Germany"
        "france" -> "France"
        "italy" -> "Italy"
        "portugal" -> "Portugal"
        "netherlands" -> "Netherlands"
        "england", "united kingdom" -> "England"
        else -> country
    }
}