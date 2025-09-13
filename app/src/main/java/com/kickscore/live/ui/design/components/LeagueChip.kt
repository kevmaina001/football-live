/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kickscore.live.R
import com.kickscore.live.ui.design.theme.KickScoreTheme
import com.kickscore.live.ui.design.tokens.ColorUtils
import com.kickscore.live.ui.design.tokens.KickScoreTypography
import com.kickscore.live.ui.design.tokens.Spacing

@Composable
fun LeagueChip(
    name: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    logoResId: Int? = null,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor = if (isSelected) {
        ColorUtils.getLeagueColor(name).copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val borderColor = if (isSelected) {
        ColorUtils.getLeagueColor(name)
    } else {
        Color.Transparent
    }

    val textColor = if (isSelected) {
        ColorUtils.getLeagueColor(name)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.Chip.cornerRadius))
            .background(backgroundColor)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(Spacing.Chip.cornerRadius)
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(
                horizontal = Spacing.Chip.paddingHorizontal,
                vertical = Spacing.Chip.paddingVertical
            ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Chip.iconSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // League logo
        logoResId?.let { resId ->
            Image(
                painter = painterResource(id = resId),
                contentDescription = "$name logo",
                modifier = Modifier.size(16.dp)
            )
        }

        // League name
        Text(
            text = name,
            style = KickScoreTypography.chipText,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FilterableLeagueChip(
    name: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    logoResId: Int? = null
) {
    LeagueChip(
        name = name,
        modifier = modifier,
        isSelected = isSelected,
        logoResId = logoResId,
        onClick = onToggle
    )
}

@Preview(showBackground = true)
@Composable
private fun LeagueChipPreview() {
    KickScoreTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LeagueChip(
                name = "Premier League",
                isSelected = false
            )
            LeagueChip(
                name = "Champions League",
                isSelected = true
            )
            LeagueChip(
                name = "La Liga",
                isSelected = false,
                logoResId = R.drawable.ic_launcher_foreground
            )
        }
    }
}