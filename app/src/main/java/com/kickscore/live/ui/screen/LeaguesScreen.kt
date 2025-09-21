/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class MockLeague(
    val id: Int,
    val name: String,
    val country: String,
    val season: String,
    val flag: String
)

data class MockStanding(
    val position: Int,
    val team: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
    val points: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaguesScreen() {
    var selectedLeague by remember { mutableStateOf<MockLeague?>(null) }
    var showStandings by remember { mutableStateOf(false) }

    val mockLeagues = remember {
        listOf(
            MockLeague(1, "Premier League", "England", "2024/25", "🏴󠁧󠁢󠁥󠁮󠁧󠁿"),
            MockLeague(2, "La Liga", "Spain", "2024/25", "🇪🇸"),
            MockLeague(3, "Serie A", "Italy", "2024/25", "🇮🇹"),
            MockLeague(4, "Bundesliga", "Germany", "2024/25", "🇩🇪"),
            MockLeague(5, "Ligue 1", "France", "2024/25", "🇫🇷"),
            MockLeague(6, "Champions League", "Europe", "2024/25", "🏆"),
            MockLeague(7, "Europa League", "Europe", "2024/25", "🥈"),
            MockLeague(8, "Premier League 2", "England", "2024/25", "🏴󠁧󠁢󠁥󠁮󠁧󠁿"),
            MockLeague(9, "Championship", "England", "2024/25", "🏴󠁧󠁢󠁥󠁮󠁧󠁿"),
            MockLeague(10, "MLS", "United States", "2024", "🇺🇸"),
        )
    }

    val mockStandings = remember {
        listOf(
            MockStanding(1, "Manchester City", 15, 12, 2, 1, 35, 12, 23, 38),
            MockStanding(2, "Arsenal", 15, 11, 3, 1, 32, 14, 18, 36),
            MockStanding(3, "Liverpool", 15, 10, 4, 1, 31, 16, 15, 34),
            MockStanding(4, "Chelsea", 15, 9, 3, 3, 28, 18, 10, 30),
            MockStanding(5, "Newcastle", 15, 8, 5, 2, 26, 20, 6, 29),
            MockStanding(6, "Manchester United", 15, 8, 2, 5, 24, 22, 2, 26),
            MockStanding(7, "Tottenham", 15, 7, 4, 4, 25, 21, 4, 25),
            MockStanding(8, "Brighton", 15, 6, 6, 3, 23, 19, 4, 24),
            MockStanding(9, "West Ham", 15, 6, 5, 4, 21, 20, 1, 23),
            MockStanding(10, "Aston Villa", 15, 6, 4, 5, 20, 19, 1, 22),
        )
    }

    if (showStandings && selectedLeague != null) {
        StandingsScreen(
            league = selectedLeague!!,
            standings = mockStandings,
            onBack = {
                showStandings = false
                selectedLeague = null
            }
        )
    } else {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "🏆 Football Leagues",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Text(
                text = "Popular Leagues",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(mockLeagues.take(5)) { league ->
            LeagueCard(
                league = league,
                onClick = {
                    selectedLeague = league
                    showStandings = true
                }
            )
        }

        item {
            Text(
                text = "Other Competitions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        items(mockLeagues.drop(5)) { league ->
            LeagueCard(
                league = league,
                onClick = {
                    selectedLeague = league
                    showStandings = true
                }
            )
        }
    }
    }
}

@Composable
fun LeagueCard(
    league: MockLeague,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag/Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = league.flag,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // League info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = league.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = league.country,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Season: ${league.season}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Arrow or status
            Text(
                text = "›",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen(
    league: MockLeague,
    standings: List<MockStanding>,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header with back button
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = league.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Season ${league.season}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                TextButton(onClick = onBack) {
                    Text("← Back")
                }
            }
        )

        // Standings table
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                StandingsHeader()
            }

            itemsIndexed(standings) { index, standing ->
                StandingRow(standing = standing)
            }
        }
    }
}

@Composable
private fun StandingsHeader() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Team",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "P",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "W",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "D",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "L",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "GD",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Pts",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StandingRow(standing: MockStanding) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = getPositionColor(standing.position)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Position
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        getPositionBackgroundColor(standing.position),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = standing.position.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Team name
            Text(
                text = standing.team,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )

            // Stats
            Text(
                text = standing.played.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.won.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.drawn.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = standing.lost.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = if (standing.goalDifference >= 0) "+${standing.goalDifference}" else "${standing.goalDifference}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center,
                color = if (standing.goalDifference >= 0) Color.Green else Color.Red,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = standing.points.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun getPositionColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50).copy(alpha = 0.1f) // Champions League - green
        in 5..6 -> Color(0xFFFFC107).copy(alpha = 0.1f) // Europa League - amber
        in 18..20 -> Color(0xFFF44336).copy(alpha = 0.1f) // Relegation - red
        else -> Color.Transparent
    }
}

private fun getPositionBackgroundColor(position: Int): Color {
    return when (position) {
        in 1..4 -> Color(0xFF4CAF50) // Champions League - green
        in 5..6 -> Color(0xFFFFC107) // Europa League - amber
        in 18..20 -> Color(0xFFF44336) // Relegation - red
        else -> Color(0xFF757575) // Default - gray
    }
}