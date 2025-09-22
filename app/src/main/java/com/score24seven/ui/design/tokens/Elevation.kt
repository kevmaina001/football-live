/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.design.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Elevation {
    // Material Design 3 elevation levels
    val level0 = 0.dp         // Surface level
    val level1 = 1.dp         // Elevated cards
    val level2 = 3.dp         // Floating action buttons
    val level3 = 6.dp         // Navigation drawer
    val level4 = 8.dp         // Modal bottom sheets
    val level5 = 12.dp        // Navigation bar

    // Custom elevation for specific components
    object Card {
        val default = level1      // 1dp
        val elevated = level2     // 3dp
        val floating = level3     // 6dp
    }

    object Button {
        val default = level0      // 0dp (outlined/text buttons)
        val filled = level1       // 1dp
        val fab = level2          // 3dp
    }

    object Navigation {
        val bottomBar = level2    // 3dp
        val drawer = level3       // 6dp
        val rail = level0         // 0dp
    }

    object Modal {
        val sheet = level4        // 8dp
        val dialog = level5       // 12dp
        val menu = level2         // 3dp
    }

    object AppBar {
        val default = level0      // 0dp (scrolled state determines elevation)
        val scrolled = level2     // 3dp
    }

    // Match-specific elevation
    object Match {
        val liveCard = level2     // 3dp (elevated for live matches)
        val scheduledCard = level1 // 1dp
        val resultCard = level1   // 1dp
    }

    // League-specific elevation
    object League {
        val header = level0       // 0dp
        val card = level1         // 1dp
    }
}

// Elevation utilities
object ElevationUtils {
    fun getCardElevation(isHighlighted: Boolean = false, isInteractive: Boolean = false): Dp {
        return when {
            isHighlighted -> Elevation.Card.floating
            isInteractive -> Elevation.Card.elevated
            else -> Elevation.Card.default
        }
    }

    fun getMatchElevation(isLive: Boolean = false): Dp {
        return if (isLive) Elevation.Match.liveCard else Elevation.Match.scheduledCard
    }

    fun getButtonElevation(isFilled: Boolean = false, isFab: Boolean = false): Dp {
        return when {
            isFab -> Elevation.Button.fab
            isFilled -> Elevation.Button.filled
            else -> Elevation.Button.default
        }
    }
}