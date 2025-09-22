/*
 * Connected Competition Matches Screen
 * Maintains date filtering connection with parent competitions screen
 */

package com.score24seven.ui.screen

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.score24seven.domain.model.League
import com.score24seven.domain.model.Match
import com.score24seven.ui.components.ErrorState
import com.score24seven.ui.components.LoadingState
import com.score24seven.ui.components.EmptyState
import com.score24seven.ui.components.ModernMatchCard
import com.score24seven.ui.design.tokens.Spacing
import com.score24seven.ui.state.UiState
import com.score24seven.ui.viewmodel.MatchesViewModel
import com.score24seven.util.DateUtils
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedCompetitionMatchesScreen(
    competitionId: Int,
    initialDateIndex: Int = 2, // Default to today if not specified
    onNavigateBack: () -> Unit,
    onNavigateBackWithDate: (Int) -> Unit, // Navigate back to competitions with specific date
    onNavigateToMatchDetail: (Match) -> Unit,
    viewModel: MatchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dates = remember { generateConnectedDatesList() }
    var selectedDateIndex by remember { mutableIntStateOf(initialDateIndex) }

    // Debug logging
    LaunchedEffect(selectedDateIndex) {
        val selectedDate = dates[selectedDateIndex]
        println("DEBUG: ConnectedCompetitionMatchesScreen - Loading matches for competition $competitionId on date: $selectedDate (index: $selectedDateIndex)")
        viewModel.loadMatchesForDate(selectedDate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628)) // Match the competition screen theme
    ) {
        // Connected Competition Header with date info
        ConnectedCompetitionHeader(
            competition = when (val matches = state.matches) {
                is UiState.Success -> getCompetitionFromMatches(matches.data, competitionId)
                else -> null
            },
            selectedDate = dates[selectedDateIndex],
            onNavigateBack = onNavigateBack
        )

        // Connected Date Tabs that communicate with parent
        ConnectedDateTabRow(
            dates = dates,
            selectedDateIndex = selectedDateIndex,
            onDateSelected = { newDateIndex ->
                println("DEBUG: ConnectedCompetitionMatchesScreen - Date changed to index=$newDateIndex, navigating back to competitions")
                // When date changes, navigate back to competitions screen with new date
                onNavigateBackWithDate(newDateIndex)
            }
        )

        // Competition Matches Content filtered by date
        AnimatedContent(
            targetState = selectedDateIndex,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier.fillMaxSize(),
            label = "connected_competition_matches_content"
        ) { dateIndex ->
            ConnectedCompetitionMatchesContent(
                matches = state.matches,
                competitionId = competitionId,
                selectedDate = dates[dateIndex],
                onMatchClick = onNavigateToMatchDetail
            )
        }
    }
}

@Composable
private fun ConnectedCompetitionHeader(
    competition: League?,
    selectedDate: LocalDate,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Top Bar with back button
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
                    contentDescription = "Back to competitions",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = competition?.name ?: "Competition",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Matches for ${DateUtils.formatDisplayDate(selectedDate.atStartOfDay())}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConnectedDateTabRow(
    dates: List<LocalDate>,
    selectedDateIndex: Int,
    onDateSelected: (Int) -> Unit
) {
    println("DEBUG: ConnectedDateTabRow - Rendering ${dates.size} dates, selected: $selectedDateIndex")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E2A3A)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dates.forEachIndexed { index, date ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedDateIndex == index) {
                            Color(0xFFDC143C)
                        } else {
                            Color.Transparent
                        }
                    ),
                    onClick = {
                        println("DEBUG: ConnectedDateTabRow - Date tab clicked: index=$index, date=$date")
                        onDateSelected(index)
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
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
                            lineHeight = 10.sp,
                            fontWeight = if (selectedDateIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = "${date.dayOfMonth}.${String.format("%02d", date.monthValue)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selectedDateIndex == index) Color.White else Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                            lineHeight = 10.sp,
                            fontWeight = if (selectedDateIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectedCompetitionMatchesContent(
    matches: UiState<List<Match>>,
    competitionId: Int,
    selectedDate: LocalDate,
    onMatchClick: (Match) -> Unit
) {
    println("DEBUG: ConnectedCompetitionMatchesContent - Competition $competitionId, Date: $selectedDate, State: ${matches::class.simpleName}")

    when (matches) {
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
                        text = "Loading matches for ${DateUtils.formatDisplayDate(selectedDate.atStartOfDay())}...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }

        is UiState.Success -> {
            // Filter matches for this specific competition and date
            val competitionMatches = matches.data.filter { match ->
                match.league.id == competitionId &&
                match.fixture.dateTime.toLocalDate() == selectedDate
            }

            println("DEBUG: ConnectedCompetitionMatchesContent - Found ${competitionMatches.size} matches for competition $competitionId on $selectedDate")

            if (competitionMatches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        message = "No matches scheduled for ${DateUtils.formatDisplayDate(selectedDate.atStartOfDay())}"
                    )
                }
            } else {
                val sortedMatches = competitionMatches.sortedBy { it.fixture.timestamp }
                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Date header
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFB000).copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = "MATCHES FOR ${DateUtils.formatDisplayDate(selectedDate.atStartOfDay()).uppercase()}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFFB000),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    // Competition matches
                    items(sortedMatches) { match ->
                        ModernMatchCard(
                            match = match,
                            onClick = { onMatchClick(match) },
                            onLiveClick = { /* TODO: Handle live click */ },
                            onFavoriteClick = { /* TODO: Handle favorite click */ }
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ErrorState(
                        message = matches.message,
                        onRetry = null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Competition: $competitionId | Date: ${DateUtils.formatDisplayDate(selectedDate.atStartOfDay())}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Helper function to find competition info from matches
private fun getCompetitionFromMatches(matches: List<Match>, competitionId: Int): League? {
    return matches.find { it.league.id == competitionId }?.league
}

private fun generateConnectedDatesList(): List<LocalDate> {
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

    println("DEBUG: generateConnectedDatesList - Generated dates:")
    datesList.forEachIndexed { index, date ->
        println("DEBUG: Date $index: $date")
    }

    return datesList
}