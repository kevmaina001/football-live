/*
 * Fixed Matches Screen for KickScore Live
 * Addresses date filtering and UI alignment issues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kickscore.live.domain.model.Match
import com.kickscore.live.ui.components.ErrorState
import com.kickscore.live.ui.components.LoadingState
import com.kickscore.live.ui.components.EmptyState
import com.kickscore.live.ui.components.ModernMatchCard
import com.kickscore.live.ui.design.tokens.Spacing
import com.kickscore.live.ui.state.UiState
import com.kickscore.live.ui.viewmodel.MatchesViewModel
import java.time.LocalDate
import com.kickscore.live.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixedMatchesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMatchDetail: (Int) -> Unit,
    viewModel: MatchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dates = remember { generateFixedDatesList() }
    var selectedDateIndex by remember { mutableIntStateOf(2) } // Default to today

    // Debug logging
    LaunchedEffect(selectedDateIndex) {
        val selectedDate = dates[selectedDateIndex]
        println("DEBUG: FixedMatchesScreen - Loading matches for date: $selectedDate (index: $selectedDateIndex)")
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
        // Header
        FixedMatchesHeader(onNavigateBack = onNavigateBack)

        // Date Tabs with LazyRow for better alignment
        FixedDateTabRow(
            dates = dates,
            selectedDateIndex = selectedDateIndex,
            onDateSelected = { index ->
                println("DEBUG: FixedMatchesScreen - Date tab selected: index=$index, date=${dates[index]}")
                selectedDateIndex = index
            }
        )

        // Matches Content
        AnimatedContent(
            targetState = state.selectedDate,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier.fillMaxSize(),
            label = "matches_content"
        ) { selectedDate ->
            FixedMatchesContent(
                matches = state.matches,
                selectedDate = selectedDate,
                onMatchClick = onNavigateToMatchDetail
            )
        }
    }
}

@Composable
private fun FixedMatchesHeader(
    onNavigateBack: () -> Unit
) {
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

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "All Matches",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "Browse matches by date",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FixedDateTabRow(
    dates: List<LocalDate>,
    selectedDateIndex: Int,
    onDateSelected: (Int) -> Unit
) {
    println("DEBUG: FixedDateTabRow - Rendering ${dates.size} dates, selected: $selectedDateIndex")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            itemsIndexed(dates) { index, date ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .defaultMinSize(minWidth = 80.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedDateIndex == index) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    onClick = {
                        println("DEBUG: FixedDateTabRow - Tab clicked: index=$index, date=$date")
                        onDateSelected(index)
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = when (index) {
                                0 -> DateUtils.formatWeekday(date.atStartOfDay())
                                1 -> "Yesterday"
                                2 -> "Today"
                                3 -> "Tomorrow"
                                else -> DateUtils.formatWeekday(date.atStartOfDay())
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selectedDateIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedDateIndex == index) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = DateUtils.formatShortDate(date.atStartOfDay()),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selectedDateIndex == index) {
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FixedMatchesContent(
    matches: UiState<List<Match>>,
    selectedDate: LocalDate,
    onMatchClick: (Int) -> Unit
) {
    println("DEBUG: FixedMatchesContent - Rendering matches for date: $selectedDate, state: ${matches::class.simpleName}")

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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        is UiState.Success -> {
            println("DEBUG: FixedMatchesContent - Success with ${matches.data.size} matches")
            if (matches.data.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        message = "No matches scheduled for ${
                            DateUtils.formatDisplayDate(selectedDate.atStartOfDay())
                        }"
                    )
                }
            } else {
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.xs),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    items(matches.data) { match ->
                        ModernMatchCard(
                            match = match,
                            onClick = { onMatchClick(match.id) },
                            onLiveClick = { /* TODO: Handle live click */ },
                            onFavoriteClick = { /* TODO: Handle favorite click */ }
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            println("DEBUG: FixedMatchesContent - Error: ${matches.message}")
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
                        text = "Date: ${DateUtils.formatDisplayDate(selectedDate.atStartOfDay())}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

private fun generateFixedDatesList(): List<LocalDate> {
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

    println("DEBUG: generateFixedDatesList - Generated dates:")
    datesList.forEachIndexed { index, date ->
        println("DEBUG: Date $index: $date")
    }

    return datesList
}