/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kickscore.live.ui.design.theme.KickScoreTheme
import com.kickscore.live.ui.design.theme.extendedColors
import com.kickscore.live.ui.design.tokens.KickScoreTypography
import com.kickscore.live.ui.design.tokens.Spacing

@Composable
fun ScoreChip(
    homeScore: Int,
    awayScore: Int,
    modifier: Modifier = Modifier,
    isLive: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.Chip.cornerRadius))
            .background(backgroundColor)
            .padding(
                horizontal = Spacing.Chip.paddingHorizontal,
                vertical = Spacing.Chip.paddingVertical
            ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home score
        Text(
            text = homeScore.toString(),
            style = KickScoreTypography.scoreDisplay.copy(
                fontSize = if (isLive) KickScoreTypography.scoreDisplay.fontSize
                          else KickScoreTypography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold
            ),
            color = if (homeScore > awayScore) MaterialTheme.extendedColors.scoreHome
                   else MaterialTheme.colorScheme.onSurface
        )

        // Separator
        Text(
            text = ":",
            style = KickScoreTypography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Away score
        Text(
            text = awayScore.toString(),
            style = KickScoreTypography.scoreDisplay.copy(
                fontSize = if (isLive) KickScoreTypography.scoreDisplay.fontSize
                          else KickScoreTypography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold
            ),
            color = if (awayScore > homeScore) MaterialTheme.extendedColors.scoreAway
                   else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreChipPreview() {
    KickScoreTheme {
        ScoreChip(
            homeScore = 2,
            awayScore = 1,
            isLive = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreChipDrawPreview() {
    KickScoreTheme {
        ScoreChip(
            homeScore = 1,
            awayScore = 1,
            isLive = false
        )
    }
}