/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.score24seven.domain.model.*

@Composable
fun CleanEventCard(event: MatchEvent) {
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
            // Time
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp)
            ) {
                Text(
                    text = "${event.time.elapsed}'",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                event.time.extra?.let { extra ->
                    Text(
                        text = "+$extra",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Event icon
            Text(
                text = event.type.iconCode,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = 16.dp)
            )

            // Event details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.type.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFFFFB000),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = event.player.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
                event.assist?.let { assist ->
                    Text(
                        text = "Assist: ${assist.name}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
            }

            // Team indicator
            Text(
                text = event.team.getInitials(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun CleanLineupCard(lineup: Lineup) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Team header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lineup.team.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFFFFB000),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = lineup.formation,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coach
            Text(
                text = "Coach: ${lineup.coach.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Starting XI
            Text(
                text = "Starting XI",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            lineup.startingEleven.forEach { lineupPlayer ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lineupPlayer.player.number?.toString() ?: "-",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFFFB000),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.width(40.dp)
                    )
                    Text(
                        text = lineupPlayer.player.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = lineupPlayer.position,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CleanStatisticsCard(statistics: List<MatchStatistic>) {
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
                text = "Match Statistics",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFFFFB000),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            statistics.forEach { stat ->
                StatisticRow(statistic = stat)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun StatisticRow(statistic: MatchStatistic) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statistic.homeValue ?: "0",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = statistic.type,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF)
                )
            )
            Text(
                text = statistic.awayValue ?: "0",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Progress bar for percentages
        statistic.homePercentage?.let { homePercent ->
            statistic.awayPercentage?.let { awayPercent ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(homePercent / 100f)
                            .height(6.dp)
                            .background(
                                Color(0xFF10B981),
                                RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .weight(awayPercent / 100f)
                            .height(6.dp)
                            .background(
                                Color(0xFFEF4444),
                                RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyTabContent(
    icon: String,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9CA3AF)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}