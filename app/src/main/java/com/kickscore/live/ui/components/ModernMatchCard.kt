/*
 * Modern Match Card Component for KickScore Live
 */

package com.kickscore.live.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.model.getScoreDisplay
import com.kickscore.live.domain.model.getTimeDisplay
import com.kickscore.live.domain.model.hasScore
import com.kickscore.live.domain.model.isLive
import com.kickscore.live.ui.design.components.TeamRow
import com.kickscore.live.ui.design.tokens.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernMatchCard(
    match: Match,
    onClick: () -> Unit,
    onLiveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(match.isFavorite) }
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1.0f,
        label = "favorite_scale"
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.xs, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (match.isLive()) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            // Header with league and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // League name
                Text(
                    text = match.league.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Live indicator
                    if (match.isLive()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    Color.Red,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .clickable { onLiveClick() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Live",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "LIVE",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Favorite button
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavoriteClick()
                        },
                        modifier = Modifier
                            .size(24.dp)
                            .scale(scale)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Teams and Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Team
                TeamRow(
                    teamName = match.homeTeam.name,
                    logoUrl = match.homeTeam.logo,
                    isHome = true,
                    compact = true,
                    modifier = Modifier.weight(1f)
                )

                // Score/Time Section
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .background(
                            if (match.isLive()) {
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Red.copy(alpha = 0.1f),
                                        Color.Red.copy(alpha = 0.2f)
                                    )
                                )
                            } else {
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    )
                                )
                            },
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (match.hasScore()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = match.getScoreDisplay(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (match.status.elapsed != null) {
                                Text(
                                    text = "${match.status.elapsed}'",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (match.isLive()) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Scheduled",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = match.getTimeDisplay(),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Away Team
                TeamRow(
                    teamName = match.awayTeam.name,
                    logoUrl = match.awayTeam.logo,
                    isHome = false,
                    compact = true,
                    modifier = Modifier.weight(1f)
                )
            }

            // Match details - only show venue for more compact display
            match.venue?.let { venue ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📍 ${venue.name}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
            }
        }
    }
}

