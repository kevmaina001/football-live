/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.design.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kickscore.live.ui.design.theme.KickScoreTheme
import com.kickscore.live.ui.design.tokens.Spacing
import kotlin.math.abs

@Composable
fun TeamInitialsLogo(
    teamName: String,
    modifier: Modifier = Modifier,
    size: Dp = Spacing.Match.logoSize
) {
    val initials = getTeamInitials(teamName)
    val backgroundColor = generateColorFromName(teamName)
    val textColor = getContrastingTextColor(backgroundColor)

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            drawCircle(
                color = backgroundColor,
                radius = size.toPx() / 2
            )
        }

        Text(
            text = initials,
            fontSize = (size.value * 0.35).sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

private fun getTeamInitials(teamName: String): String {
    val words = teamName.split(" ")
    return when {
        words.size >= 2 -> {
            "${words[0].firstOrNull()?.uppercase() ?: ""}${words[1].firstOrNull()?.uppercase() ?: ""}"
        }
        words.size == 1 && teamName.length >= 2 -> {
            teamName.take(2).uppercase()
        }
        else -> teamName.take(1).uppercase()
    }
}

private fun generateColorFromName(name: String): Color {
    // Generate a consistent color based on team name hash
    val hash = abs(name.hashCode())
    val hue = (hash % 360).toFloat()

    // Predefined colors for better visual appeal
    val colors = listOf(
        Color(0xFF1976D2), // Blue
        Color(0xFF388E3C), // Green
        Color(0xFFD32F2F), // Red
        Color(0xFF7B1FA2), // Purple
        Color(0xFFE64A19), // Deep Orange
        Color(0xFF00796B), // Teal
        Color(0xFF303F9F), // Indigo
        Color(0xFFC2185B), // Pink
        Color(0xFF5D4037), // Brown
        Color(0xFF455A64)  // Blue Grey
    )

    return colors[hash % colors.size]
}

private fun getContrastingTextColor(backgroundColor: Color): Color {
    // Calculate luminance to determine if we need light or dark text
    val red = backgroundColor.red
    val green = backgroundColor.green
    val blue = backgroundColor.blue

    val luminance = 0.299 * red + 0.587 * green + 0.114 * blue

    return if (luminance > 0.5) Color.Black else Color.White
}

@Preview(showBackground = true)
@Composable
private fun TeamInitialsLogoPreview() {
    KickScoreTheme {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            TeamInitialsLogo(teamName = "Manchester United")
            TeamInitialsLogo(teamName = "Arsenal")
            TeamInitialsLogo(teamName = "Barcelona")
            TeamInitialsLogo(teamName = "Chelsea FC")
        }
    }
}