/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kickscore.live.domain.model.Fixture
import com.kickscore.live.domain.model.League
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.model.MatchStatus
import com.kickscore.live.domain.model.Score
import com.kickscore.live.domain.model.Team
import com.kickscore.live.ui.design.components.LivePill
import com.kickscore.live.ui.design.components.MatchStatusPill
import com.kickscore.live.ui.design.components.ScoreChip
import com.kickscore.live.ui.design.components.TeamRow
import com.kickscore.live.ui.design.theme.KickScoreTheme
import com.kickscore.live.ui.design.tokens.KickScoreTypography
import com.kickscore.live.ui.design.tokens.Spacing
import java.time.LocalDateTime

@Composable
fun MatchCard(
    match: Match,
    onClick: () -> Unit,
    onLiveClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (match.status.isLive) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Card.padding),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            // League and status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.league.name,
                    style = KickScoreTypography.leagueName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (match.status.isLive) {
                    MatchStatusPill(
                        status = match.status.short,
                        minute = match.status.elapsed
                    )
                } else {
                    Text(
                        text = match.getTimeDisplay(),
                        style = KickScoreTypography.matchTime,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Teams and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team
                TeamRow(
                    teamName = match.homeTeam.name,
                    logoUrl = match.homeTeam.logo,
                    isHome = true,
                    compact = true,
                    modifier = Modifier.weight(1f)
                )

                // Score
                if (match.hasScore()) {
                    ScoreChip(
                        homeScore = match.score.home!!,
                        awayScore = match.score.away!!,
                        isLive = match.status.isLive,
                        modifier = Modifier.padding(horizontal = Spacing.md)
                    )
                } else {
                    Text(
                        text = "vs",
                        style = KickScoreTypography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = Spacing.md)
                    )
                }

                // Away team
                TeamRow(
                    teamName = match.awayTeam.name,
                    logoUrl = match.awayTeam.logo,
                    isHome = false,
                    compact = true,
                    modifier = Modifier.weight(1f)
                )
            }

            // Additional info
            if (match.venue != null) {
                Text(
                    text = "${match.venue.name}${match.venue.city?.let { ", $it" } ?: ""}",
                    style = KickScoreTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchCardPreview() {
    KickScoreTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live match
            MatchCard(
                match = Match(
                    id = 1,
                    homeTeam = Team(
                        id = 1,
                        name = "Manchester United",
                        logo = null
                    ),
                    awayTeam = Team(
                        id = 2,
                        name = "Arsenal",
                        logo = null
                    ),
                    league = League(
                        id = 1,
                        name = "Premier League",
                        country = "England"
                    ),
                    fixture = Fixture(
                        dateTime = LocalDateTime.now(),
                        timezone = "UTC",
                        timestamp = System.currentTimeMillis()
                    ),
                    score = Score(home = 2, away = 1),
                    status = MatchStatus(
                        short = "LIVE",
                        long = "Match In Play",
                        elapsed = 67,
                        isLive = true,
                        isFinished = false
                    )
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            // Scheduled match
            MatchCard(
                match = Match(
                    id = 2,
                    homeTeam = Team(
                        id = 3,
                        name = "Barcelona",
                        logo = null
                    ),
                    awayTeam = Team(
                        id = 4,
                        name = "Real Madrid",
                        logo = null
                    ),
                    league = League(
                        id = 2,
                        name = "La Liga",
                        country = "Spain"
                    ),
                    fixture = Fixture(
                        dateTime = LocalDateTime.now().plusHours(2),
                        timezone = "UTC",
                        timestamp = System.currentTimeMillis() + 7200000
                    ),
                    score = Score(home = null, away = null),
                    status = MatchStatus(
                        short = "NS",
                        long = "Not Started",
                        elapsed = null,
                        isLive = false,
                        isFinished = false
                    )
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}