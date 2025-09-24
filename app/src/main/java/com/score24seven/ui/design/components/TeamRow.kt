/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.score24seven.R
import com.score24seven.ui.design.theme.Score24SevenTheme
import com.score24seven.ui.design.tokens.Score24SevenTypography
import com.score24seven.ui.design.tokens.Spacing

@Composable
fun TeamRow(
    teamName: String,
    modifier: Modifier = Modifier,
    logoUrl: String? = null,
    logoResId: Int? = null,
    isHome: Boolean = true,
    subtitle: String? = null,
    compact: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),
        horizontalArrangement = if (isHome) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isHome) {
            // Home team: Logo - Name
            TeamLogo(
                logoUrl = logoUrl,
                logoResId = logoResId,
                teamName = teamName,
                size = if (compact) Spacing.Icon.medium else Spacing.Match.logoSize
            )

            Spacer(modifier = Modifier.width(Spacing.Match.logoSpacing))

            TeamInfo(
                teamName = teamName,
                subtitle = subtitle,
                textAlign = TextAlign.Start,
                compact = compact
            )
        } else {
            // Away team: Name - Logo
            TeamInfo(
                teamName = teamName,
                subtitle = subtitle,
                textAlign = TextAlign.End,
                compact = compact
            )

            Spacer(modifier = Modifier.width(Spacing.Match.logoSpacing))

            TeamLogo(
                logoUrl = logoUrl,
                logoResId = logoResId,
                teamName = teamName,
                size = if (compact) Spacing.Icon.medium else Spacing.Match.logoSize
            )
        }
    }
}

@Composable
private fun TeamLogo(
    logoUrl: String?,
    logoResId: Int?,
    teamName: String,
    size: androidx.compose.ui.unit.Dp
) {
    when {
        logoUrl != null -> {
            AsyncImage(
                model = logoUrl,
                contentDescription = "$teamName logo",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.ic_launcher_foreground)
            )
        }
        logoResId != null -> {
            Image(
                painter = painterResource(id = logoResId),
                contentDescription = "$teamName logo",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        else -> {
            // Placeholder with team initials
            TeamInitialsLogo(
                teamName = teamName,
                size = size
            )
        }
    }
}

@Composable
private fun TeamInitialsLogo(
    teamName: String,
    size: androidx.compose.ui.unit.Dp
) {
    val initials = teamName.split(" ")
        .take(2)
        .map { it.firstOrNull()?.uppercase() ?: "" }
        .joinToString("")
        .take(2)

    Surface(
        modifier = Modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun TeamInfo(
    teamName: String,
    subtitle: String?,
    textAlign: TextAlign,
    compact: Boolean
) {
    Column(
        horizontalAlignment = if (textAlign == TextAlign.Start) {
            Alignment.Start
        } else {
            Alignment.End
        }
    ) {
        Text(
            text = teamName,
            style = if (compact) Score24SevenTypography.titleSmall else Score24SevenTypography.teamName,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        subtitle?.let { sub ->
            Text(
                text = sub,
                style = Score24SevenTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = textAlign,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamRowPreview() {
    Score24SevenTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TeamRow(
                teamName = "Manchester United",
                isHome = true,
                logoResId = R.drawable.ic_launcher_foreground,
                subtitle = "Premier League"
            )

            TeamRow(
                teamName = "Arsenal FC",
                isHome = false,
                logoResId = R.drawable.ic_launcher_foreground,
                subtitle = "Premier League"
            )

            TeamRow(
                teamName = "Barcelona",
                isHome = true,
                compact = true
            )
        }
    }
}