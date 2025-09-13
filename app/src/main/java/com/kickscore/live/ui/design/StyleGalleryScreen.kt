/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kickscore.live.R
import com.kickscore.live.ui.design.components.LeagueChip
import com.kickscore.live.ui.design.components.LivePill
import com.kickscore.live.ui.design.components.MatchStatusPill
import com.kickscore.live.ui.design.components.ScoreChip
import com.kickscore.live.ui.design.components.TeamInitialsLogo
import com.kickscore.live.ui.design.components.TeamRow
import com.kickscore.live.ui.design.theme.KickScoreTheme
import com.kickscore.live.ui.design.theme.extendedColors
import com.kickscore.live.ui.design.tokens.Colors
import com.kickscore.live.ui.design.tokens.KickScoreTypography
import com.kickscore.live.ui.design.tokens.Spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StyleGalleryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(Spacing.Screen.horizontal),
        verticalArrangement = Arrangement.spacedBy(Spacing.xl)
    ) {
        // Header
        Text(
            text = "KickScore Design System",
            style = KickScoreTypography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = Spacing.xl)
        )

        // Colors Section
        StyleSection(title = "Colors") {
            ColorPalette()
        }

        // Typography Section
        StyleSection(title = "Typography") {
            TypographyShowcase()
        }

        // Components Section
        StyleSection(title = "Score Components") {
            ScoreComponentsShowcase()
        }

        StyleSection(title = "Status Components") {
            StatusComponentsShowcase()
        }

        StyleSection(title = "Team Components") {
            TeamComponentsShowcase()
        }

        StyleSection(title = "League Components") {
            LeagueComponentsShowcase()
        }

        // Spacing demonstration
        StyleSection(title = "Spacing & Layout") {
            SpacingShowcase()
        }

        Spacer(modifier = Modifier.height(Spacing.huge))
    }
}

@Composable
private fun StyleSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(
            text = title,
            style = KickScoreTypography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.lg)
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorPalette() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        ColorSwatch("Primary", Colors.Primary)
        ColorSwatch("Accent", Colors.Accent)
        ColorSwatch("Success", Colors.Success)
        ColorSwatch("Danger", Colors.Danger)
        ColorSwatch("Warning", Colors.Warning)
        ColorSwatch("Live", Colors.LiveIndicator)
        ColorSwatch("Score Home", Colors.ScoreHome)
        ColorSwatch("Score Away", Colors.ScoreAway)
    }
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        Text(
            text = name,
            style = KickScoreTypography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TypographyShowcase() {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(
            text = "Display Large - Hero Text",
            style = KickScoreTypography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Headline Large - Section Headers",
            style = KickScoreTypography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Title Large - Card Titles",
            style = KickScoreTypography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Body Large - Main content text for readability",
            style = KickScoreTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Team Name Style",
            style = KickScoreTypography.teamName,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Score Display: 2-1",
            style = KickScoreTypography.scoreDisplay,
            color = MaterialTheme.extendedColors.scoreHome
        )
    }
}

@Composable
private fun ScoreComponentsShowcase() {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            ScoreChip(homeScore = 2, awayScore = 1, isLive = true)
            ScoreChip(homeScore = 0, awayScore = 3, isLive = false)
            ScoreChip(homeScore = 1, awayScore = 1, isLive = false)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatusComponentsShowcase() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        LivePill()
        MatchStatusPill(status = "LIVE", minute = 67)
        MatchStatusPill(status = "HT")
        MatchStatusPill(status = "FT")
        MatchStatusPill(status = "SCHEDULED")
    }
}

@Composable
private fun TeamComponentsShowcase() {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        TeamRow(
            teamName = "Manchester United",
            isHome = true,
            logoResId = R.drawable.ic_launcher_foreground,
            subtitle = "Premier League"
        )

        Divider(color = MaterialTheme.colorScheme.outline)

        TeamRow(
            teamName = "Arsenal FC",
            isHome = false,
            subtitle = "Premier League"
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            TeamInitialsLogo(teamName = "Manchester United")
            TeamInitialsLogo(teamName = "Arsenal")
            TeamInitialsLogo(teamName = "Barcelona")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LeagueComponentsShowcase() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        LeagueChip(name = "Premier League", isSelected = false)
        LeagueChip(name = "Champions League", isSelected = true)
        LeagueChip(name = "La Liga", isSelected = false)
        LeagueChip(name = "Serie A", isSelected = false)
        LeagueChip(name = "Bundesliga", isSelected = false)
    }
}

@Composable
private fun SpacingShowcase() {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(
            text = "Spacing Examples",
            style = KickScoreTypography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            SpacingSwatch("XS", Spacing.xs)
            SpacingSwatch("SM", Spacing.sm)
            SpacingSwatch("MD", Spacing.md)
            SpacingSwatch("LG", Spacing.lg)
            SpacingSwatch("XL", Spacing.xl)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier.size(Spacing.Icon.small),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(Spacing.Icon.medium),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier.size(Spacing.Icon.large),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SpacingSwatch(
    name: String,
    size: androidx.compose.ui.unit.Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(size)
                .height(24.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(2.dp)
                )
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        Text(
            text = name,
            style = KickScoreTypography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StyleGalleryPreview() {
    KickScoreTheme {
        StyleGalleryScreen()
    }
}