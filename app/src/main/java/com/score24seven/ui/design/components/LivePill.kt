/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.design.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.score24seven.ui.design.theme.Score24SevenTheme
import com.score24seven.ui.design.theme.extendedColors
import com.score24seven.ui.design.tokens.Score24SevenTypography
import com.score24seven.ui.design.tokens.Spacing

@Composable
fun LivePill(
    text: String = "LIVE",
    modifier: Modifier = Modifier,
    isBlinking: Boolean = true,
    backgroundColor: Color = MaterialTheme.extendedColors.liveIndicator,
    textColor: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LivePillTransition")

    val alpha by infiniteTransition.animateFloat(
        initialValue = if (isBlinking) 0.3f else 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "LivePillAlpha"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.Chip.cornerRadius))
            .background(backgroundColor)
            .alpha(if (isBlinking) alpha else 1f)
            .padding(
                horizontal = Spacing.sm,
                vertical = Spacing.xs
            ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Live indicator dot
        Box(
            modifier = Modifier.size(6.dp)
        ) {
            Canvas(modifier = Modifier.size(6.dp)) {
                drawCircle(
                    color = textColor,
                    radius = 3.dp.toPx()
                )
            }
        }

        // Live text
        Text(
            text = text,
            style = Score24SevenTypography.chipText.copy(
                fontWeight = FontWeight.Bold
            ),
            color = textColor
        )
    }
}

@Composable
fun MatchStatusPill(
    status: String,
    modifier: Modifier = Modifier,
    minute: Int? = null
) {
    val (backgroundColor, textColor, isBlinking) = when (status.uppercase()) {
        "LIVE", "1H", "2H" -> Triple(
            MaterialTheme.extendedColors.liveIndicator,
            Color.White,
            true
        )
        "HT" -> Triple(
            MaterialTheme.extendedColors.warning,
            Color.White,
            false
        )
        "FT" -> Triple(
            MaterialTheme.extendedColors.success,
            Color.White,
            false
        )
        "SCHEDULED", "NS" -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            false
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            false
        )
    }

    val displayText = when {
        status.uppercase() in listOf("LIVE", "1H", "2H") && minute != null -> "${minute}'"
        status.uppercase() == "HT" -> "HT"
        status.uppercase() == "FT" -> "FT"
        else -> status
    }

    LivePill(
        text = displayText,
        modifier = modifier,
        isBlinking = isBlinking,
        backgroundColor = backgroundColor,
        textColor = textColor
    )
}

@Preview(showBackground = true)
@Composable
private fun LivePillPreview() {
    Score24SevenTheme {
        LivePill()
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchStatusPillPreview() {
    Score24SevenTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MatchStatusPill(status = "LIVE", minute = 45)
            MatchStatusPill(status = "HT")
            MatchStatusPill(status = "FT")
            MatchStatusPill(status = "SCHEDULED")
        }
    }
}