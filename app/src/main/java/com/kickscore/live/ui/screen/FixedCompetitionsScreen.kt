/*
 * Fixed Competitions Screen with date filtering functionality
 * Shows competitions available for selected dates
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
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.ui.viewmodel.MatchesViewModel
import com.kickscore.live.util.DateUtils
import com.kickscore.live.ui.navigation.CompetitionDateCache
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixedCompetitionsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCompetition: (Int, Int) -> Unit,
    viewModel: MatchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dates = remember { generateCompetitionDatesList() }
    var selectedDateIndex by remember {
        mutableIntStateOf(CompetitionDateCache.getDateIndex()) // Get from cache or default to today
    }

    // Debug logging
    LaunchedEffect(selectedDateIndex) {
        val selectedDate = dates[selectedDateIndex]
        println("DEBUG: FixedCompetitionsScreen - Loading competitions for date: $selectedDate (index: $selectedDateIndex)")
        viewModel.loadMatchesForDate(selectedDate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628)) // Dark blue background like the reference
    ) {
        // Header with date filtering
        FixedCompetitionsHeader(
            onNavigateBack = onNavigateBack,
            dates = dates,
            selectedDateIndex = selectedDateIndex,
            onDateSelected = { index ->
                println("DEBUG: FixedCompetitionsScreen - Date tab selected: index=$index, date=${dates[index]}")
                selectedDateIndex = index
                CompetitionDateCache.setDateIndex(index) // Update cache when date is changed
            },
            competitionsCount = when (val matchesState = state.matches) {
                is UiState.Success -> analyzeCompetitions(matchesState.data).size
                else -> 0
            }
        )

        // Competitions Content based on selected date
        val matchesState = state.matches
        when (matchesState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoadingState()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading competitions for ${DateUtils.formatDisplayDate(dates[selectedDateIndex].atStartOfDay())}...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            is UiState.Success -> {
                val competitions = analyzeCompetitions(matchesState.data)
                println("DEBUG: FixedCompetitionsScreen - Found ${competitions.size} competitions for ${dates[selectedDateIndex]}")

                if (competitions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyState(
                            message = "No competitions available for ${
                                DateUtils.formatDisplayDate(dates[selectedDateIndex].atStartOfDay())
                            }"
                        )
                    }
                } else {
                    FixedCompetitionsList(
                        competitions = competitions,
                        selectedDate = dates[selectedDateIndex],
                        selectedDateIndex = selectedDateIndex,
                        onCompetitionClick = onNavigateToCompetition
                    )
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ErrorState(
                            message = matchesState.message,
                            onRetry = {
                                viewModel.loadMatchesForDate(dates[selectedDateIndex])
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Date: ${DateUtils.formatDisplayDate(dates[selectedDateIndex].atStartOfDay())}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FixedCompetitionsHeader(
    onNavigateBack: () -> Unit,
    dates: List<LocalDate>,
    selectedDateIndex: Int,
    onDateSelected: (Int) -> Unit,
    competitionsCount: Int
) {
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

            // Search icon placeholder
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight, // Placeholder for search
                    contentDescription = "Search",
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fixed Date tabs with actual functionality
        FixedDateTabsRow(
            dates = dates,
            selectedDateIndex = selectedDateIndex,
            onDateSelected = onDateSelected
        )

        Spacer(modifier = Modifier.height(20.dp))

        // All competitions header with dynamic count
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
            // Dynamic competition count badge
            Box(
                modifier = Modifier
                    .background(Color(0xFFDC143C), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = competitionsCount.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FixedDateTabsRow(
    dates: List<LocalDate>,
    selectedDateIndex: Int,
    onDateSelected: (Int) -> Unit
) {
    println("DEBUG: FixedDateTabsRow - Rendering ${dates.size} dates, selected: $selectedDateIndex")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(dates) { index, date ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .defaultMinSize(minWidth = 60.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedDateIndex == index) {
                        Color(0xFFDC143C)
                    } else {
                        Color.Transparent
                    }
                ),
                onClick = {
                    println("DEBUG: FixedDateTabsRow - Tab clicked: index=$index, date=$date")
                    onDateSelected(index)
                }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (index) {
                            0 -> DateUtils.formatWeekday(date.atStartOfDay()).uppercase()
                            1 -> "YESTERDAY"
                            2 -> "TODAY"
                            3 -> "TOMORROW"
                            else -> DateUtils.formatWeekday(date.atStartOfDay()).uppercase()
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selectedDateIndex == index) Color.White else Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp,
                        fontWeight = if (selectedDateIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                    Text(
                        text = "${date.dayOfMonth}.${String.format("%02d", date.monthValue)}.",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selectedDateIndex == index) Color.White else Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp,
                        fontWeight = if (selectedDateIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun FixedCompetitionsList(
    competitions: List<CompetitionInfo>,
    selectedDate: LocalDate,
    selectedDateIndex: Int,
    onCompetitionClick: (Int, Int) -> Unit
) {
    // Separate priority and other competitions
    val priorityCompetitions = competitions.filter { isPriorityLeague(it.league.id) }
    val otherCompetitions = competitions.filter { !isPriorityLeague(it.league.id) }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        // Date info
        item {
            Text(
                text = "COMPETITIONS FOR ${DateUtils.formatDisplayDate(selectedDate.atStartOfDay()).uppercase()}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFFB000).copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Favourite Competitions Section
        if (priorityCompetitions.isNotEmpty()) {
            item {
                Text(
                    text = "FAVOURITE COMPETITIONS",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            items(priorityCompetitions) { competition ->
                FixedCompetitionCard(
                    competition = competition,
                    onClick = { onCompetitionClick(competition.league.id, selectedDateIndex) }
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
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            items(otherCompetitions) { competition ->
                FixedCompetitionCard(
                    competition = competition,
                    onClick = { onCompetitionClick(competition.league.id, selectedDateIndex) }
                )
            }
        }
    }
}

@Composable
private fun FixedCompetitionCard(
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
                // Live indicator
                Text(
                    text = "🔴",
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

// Analyze matches to create competition information
private fun analyzeCompetitions(matches: List<Match>): List<CompetitionInfo> {
    println("DEBUG: analyzeCompetitions - Processing ${matches.size} matches")
    val matchesByLeague = matches.groupBy { it.league }
    println("DEBUG: analyzeCompetitions - Found ${matchesByLeague.size} unique leagues")

    return matchesByLeague.map { (league, leagueMatches) ->
        val competitionInfo = CompetitionInfo(
            league = league,
            totalMatches = leagueMatches.size,
            liveMatches = leagueMatches.count { it.status.isLive },
            todayMatches = leagueMatches.count { match ->
                val today = LocalDate.now()
                match.fixture.dateTime.toLocalDate() == today
            },
            upcomingMatches = leagueMatches.count { match ->
                match.fixture.dateTime.isAfter(java.time.LocalDateTime.now())
            }
        )
        println("DEBUG: analyzeCompetitions - ${league.name}: ${competitionInfo.totalMatches} matches (${competitionInfo.liveMatches} live)")
        competitionInfo
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

private fun generateCompetitionDatesList(): List<LocalDate> {
    val today = LocalDate.now()
    val datesList = listOf(
        today.minusDays(2), // 2 days ago
        today.minusDays(1), // Yesterday
        today,              // Today
        today.plusDays(1),  // Tomorrow
        today.plusDays(2),  // 2 days later
        today.plusDays(3),  // 3 days later
        today.plusDays(4)   // 4 days later
    )

    println("DEBUG: generateCompetitionDatesList - Generated dates:")
    datesList.forEachIndexed { index, date ->
        println("DEBUG: Date $index: $date")
    }

    return datesList
}