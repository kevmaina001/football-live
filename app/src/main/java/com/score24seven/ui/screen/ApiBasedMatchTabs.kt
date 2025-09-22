/*
 * API-Based Match Detail Tabs - Following Friend's Working Pattern
 * Each tab makes separate API calls to specific endpoints
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.score24seven.ui.viewmodel.MatchDetailTabsViewModel
import com.score24seven.domain.util.Resource

@Composable
fun ApiBasedHeadToHeadTab(
    fixtureId: Int,
    homeTeamId: Int,
    awayTeamId: Int,
    viewModel: MatchDetailTabsViewModel = hiltViewModel()
) {
    val headToHeadState by viewModel.headToHeadState.collectAsState()

    LaunchedEffect(homeTeamId, awayTeamId) {
        viewModel.loadHeadToHead(homeTeamId, awayTeamId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (headToHeadState) {
            is Resource.Loading -> {
                item {
                    ApiLoadingState("Loading head-to-head data...")
                }
            }
            is Resource.Error -> {
                item {
                    HeadToHeadPlaceholderContent()
                }
            }
            is Resource.Success -> {
                val matches = headToHeadState.data
                if (matches?.isNotEmpty() == true) {
                    // Display real H2H data
                    matches.forEach { match ->
                        item {
                            HeadToHeadMatchCard(match = match)
                        }
                    }
                } else {
                    item {
                        HeadToHeadPlaceholderContent()
                    }
                }
            }
        }
    }
}

@Composable
fun ApiBasedLineupsTab(
    fixtureId: Int,
    viewModel: MatchDetailTabsViewModel = hiltViewModel()
) {
    val lineupsState by viewModel.lineupsState.collectAsState()

    LaunchedEffect(fixtureId) {
        viewModel.loadLineups(fixtureId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (lineupsState) {
            is Resource.Loading -> {
                item {
                    ApiLoadingState("Loading lineups...")
                }
            }
            is Resource.Error -> {
                item {
                    LineupsPlaceholderContent()
                }
            }
            is Resource.Success -> {
                val lineups = lineupsState.data
                if (lineups?.isNotEmpty() == true) {
                    // Display real lineups data
                    lineups.forEach { lineup ->
                        item {
                            LineupCard(lineup = lineup)
                        }
                    }
                } else {
                    item {
                        LineupsPlaceholderContent()
                    }
                }
            }
        }
    }
}

@Composable
fun ApiBasedStatisticsTab(
    fixtureId: Int,
    viewModel: MatchDetailTabsViewModel = hiltViewModel()
) {
    val statisticsState by viewModel.statisticsState.collectAsState()

    LaunchedEffect(fixtureId) {
        viewModel.loadStatistics(fixtureId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (statisticsState) {
            is Resource.Loading -> {
                item {
                    ApiLoadingState("Loading statistics...")
                }
            }
            is Resource.Error -> {
                item {
                    StatisticsPlaceholderContent()
                }
            }
            is Resource.Success -> {
                val statistics = statisticsState.data
                if (statistics?.isNotEmpty() == true) {
                    // Display real statistics data
                    statistics.forEach { teamStats ->
                        item {
                            StatisticsCard(statistics = teamStats)
                        }
                    }
                } else {
                    item {
                        StatisticsPlaceholderContent()
                    }
                }
            }
        }
    }
}

@Composable
fun ApiBasedEventsTab(
    fixtureId: Int,
    viewModel: MatchDetailTabsViewModel = hiltViewModel()
) {
    val eventsState by viewModel.eventsState.collectAsState()

    LaunchedEffect(fixtureId) {
        viewModel.loadEvents(fixtureId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1628))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (eventsState) {
            is Resource.Loading -> {
                item {
                    ApiLoadingState("Loading events...")
                }
            }
            is Resource.Error -> {
                item {
                    EventsPlaceholderContent()
                }
            }
            is Resource.Success -> {
                val events = eventsState.data
                if (events?.isNotEmpty() == true) {
                    // Display real events data
                    events.forEach { event ->
                        item {
                            EventCard(event = event)
                        }
                    }
                } else {
                    item {
                        EventsPlaceholderContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiLoadingState(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFFFFB000),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ApiErrorState(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun ApiEmptyState(
    icon: String,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun PredictionCard(prediction: com.score24seven.data.api.PredictionDto) {
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
                text = "Prediction Analysis",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            prediction.predictions.winner?.let { winner ->
                Text(
                    text = "Predicted Winner: ${winner.name}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            prediction.predictions.advice?.let { advice ->
                Text(
                    text = "Advice: $advice",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF9CA3AF)
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            prediction.predictions.percent?.let { percent ->
                Text(
                    text = "Home: ${percent.home}% | Draw: ${percent.draw}% | Away: ${percent.away}%",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF10B981)
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun LineupCard(lineup: com.score24seven.data.api.LineupDto) {
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
                text = lineup.team.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Formation: ${lineup.formation}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Text(
                text = "Starting XI: ${lineup.startXI.size} players",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                ),
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Text(
                text = "Substitutes: ${lineup.substitutes.size} players",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                ),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun StatisticsCard(statistics: com.score24seven.data.api.StatisticsDto) {
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
                text = statistics.team.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            statistics.statistics.forEach { stat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stat.type,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                    Text(
                        text = stat.value ?: "N/A",
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

@Composable
private fun EventCard(event: com.score24seven.data.api.EventDto) {
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
                text = "${event.time.elapsed}'",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.type,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = event.player.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
                Text(
                    text = event.team.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }
        }
    }
}

@Composable
private fun HeadToHeadPlaceholderContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    text = "Head-to-Head Analysis",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                listOf(
                    "Last 5 Meetings" to "Available soon",
                    "Overall Record" to "Available soon",
                    "Recent Form" to "Available soon",
                    "Goals Scored" to "Available soon"
                ).forEach { (label, value) ->
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
}

@Composable
private fun LineupsPlaceholderContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A2332)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👥",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Lineups Coming Soon",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Team lineups and formations will be available closer to kick-off time",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF9CA3AF)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun StatisticsPlaceholderContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A2332)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📊",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Match Statistics",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Detailed match statistics will be available during and after the game",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF9CA3AF)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun EventsPlaceholderContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A2332)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚽",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Match Events",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Goals, cards, substitutions and other match events will appear here during the game",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF9CA3AF)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun HeadToHeadMatchCard(match: com.score24seven.data.dto.MatchDto) {
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.teams.home.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${match.goals.home ?: 0} - ${match.goals.away ?: 0}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = match.teams.away.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = match.league.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )

                Text(
                    text = match.fixture.date,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }
        }
    }
}