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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class MockLeague(
    val id: Int,
    val name: String,
    val country: String,
    val season: String,
    val flag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaguesScreen() {
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
                onClick = { /* Navigate to league details */ }
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
                onClick = { /* Navigate to league details */ }
            )
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