/*
 * Comprehensive League Details Screen with 4-tab structure like friend's app
 * Matches, Standing, Prediction, Top Scorer
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.score24seven.ui.viewmodel.LeagueDetailsViewModel
import com.score24seven.data.model.SimpleStanding
import com.score24seven.data.model.SimpleFixture
import com.score24seven.data.model.SimpleTopScorer
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueDetailsScreen(
    leagueId: Int,
    season: Int,
    leagueName: String,
    onNavigateBack: () -> Unit,
    viewModel: LeagueDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(leagueId, season) {
        viewModel.loadLeagueDetails(leagueId, season, leagueName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = leagueName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Navigation
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabTitles = listOf("Matches", "Standing", "Prediction", "Top Scorer")

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Content
        when (selectedTabIndex) {
            0 -> MatchesTab(
                fixtures = uiState.fixtures,
                isLoading = uiState.fixturesLoading,
                errorMessage = uiState.fixturesError
            )
            1 -> StandingTab(
                standings = uiState.standings,
                isLoading = uiState.standingsLoading,
                errorMessage = uiState.standingsError
            )
            2 -> PredictionTab()
            3 -> TopScorerTab(
                topScorers = uiState.topScorers,
                isLoading = uiState.topScorersLoading,
                errorMessage = uiState.topScorersError
            )
        }
    }
}

@Composable
private fun MatchesTab(
    fixtures: List<SimpleFixture>,
    isLoading: Boolean,
    errorMessage: String?
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        fixtures.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No fixtures available")
            }
        }
        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group fixtures by round (check both fixture.round and league.round)
                val fixturesByRound = fixtures.groupBy { fixture ->
                    fixture.league?.round ?: fixture.fixture?.round ?: "Unknown Round"
                }

                fixturesByRound.forEach { (round, roundFixtures) ->
                    item {
                        Text(
                            text = round,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(roundFixtures) { fixture ->
                        FixtureCard(fixture = fixture)
                    }
                }
            }
        }
    }
}

@Composable
private fun FixtureCard(fixture: SimpleFixture) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Date and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDate(fixture.fixture?.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = fixture.fixture?.status?.short ?: "N/A",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Teams and Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = fixture.teams?.home?.name ?: "Home Team",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${fixture.goals?.home ?: "-"} - ${fixture.goals?.away ?: "-"}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = fixture.teams?.away?.name ?: "Away Team",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StandingTab(
    standings: List<SimpleStanding>,
    isLoading: Boolean,
    errorMessage: String?
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        standings.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No standings available")
            }
        }
        else -> {
            LazyColumn {
                item {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pos",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(40.dp)
                        )
                        Text(
                            text = "Team",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "P",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(30.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "W",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(30.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "D",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(30.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "L",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(30.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Pts",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider()
                }

                items(standings) { standing ->
                    StandingRow(standing = standing)
                }
            }
        }
    }
}

@Composable
private fun StandingRow(standing: SimpleStanding) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${standing.rank}",
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = standing.team?.name ?: "Unknown",
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
        Text(
            text = "${standing.all?.played ?: 0}",
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "${standing.all?.win ?: 0}",
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "${standing.all?.draw ?: 0}",
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "${standing.all?.lose ?: 0}",
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "${standing.points}",
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PredictionTab() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Prediction feature coming soon")
    }
}

@Composable
private fun TopScorerTab(
    topScorers: List<SimpleTopScorer>,
    isLoading: Boolean,
    errorMessage: String?
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        topScorers.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No top scorers available")
            }
        }
        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pos",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(40.dp)
                        )
                        Text(
                            text = "Player",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Team",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(60.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Goals",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(50.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Apps",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider()
                }

                items(topScorers.take(20)) { topScorer ->
                    TopScorerRow(
                        topScorer = topScorer,
                        position = topScorers.indexOf(topScorer) + 1
                    )
                }
            }
        }
    }
}

@Composable
private fun TopScorerRow(
    topScorer: SimpleTopScorer,
    position: Int
) {
    val stats = topScorer.statistics?.firstOrNull()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$position",
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player photo
            if (topScorer.player?.photo != null) {
                AsyncImage(
                    model = topScorer.player.photo,
                    contentDescription = "${topScorer.player.name} photo",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = topScorer.player?.name?.take(1) ?: "?",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = topScorer.player?.name ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Text(
                    text = topScorer.player?.nationality ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }

        Text(
            text = stats?.team?.name?.take(3)?.uppercase() ?: "N/A",
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "${stats?.goals?.total ?: 0}",
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${stats?.games?.appearences ?: 0}",
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun formatDate(dateString: String?): String {
    if (dateString == null) return "N/A"
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        "N/A"
    }
}