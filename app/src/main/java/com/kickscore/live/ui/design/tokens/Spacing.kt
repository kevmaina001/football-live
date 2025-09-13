/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.design.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Spacing {
    // Base spacing unit - 4dp
    private val baseUnit = 4.dp

    // Micro spacing - for tight layouts
    val xs = baseUnit * 1      // 4dp
    val sm = baseUnit * 2      // 8dp
    val md = baseUnit * 3      // 12dp
    val lg = baseUnit * 4      // 16dp
    val xl = baseUnit * 5      // 20dp
    val xxl = baseUnit * 6     // 24dp

    // Macro spacing - for layout separation
    val xxxl = baseUnit * 8    // 32dp
    val huge = baseUnit * 10   // 40dp
    val massive = baseUnit * 12 // 48dp

    // Semantic spacing
    val none = 0.dp
    val hairline = 1.dp
    val thin = 2.dp

    // Component-specific spacing
    object Card {
        val padding = lg              // 16dp
        val margin = md               // 12dp
        val elevation = xs            // 4dp
        val cornerRadius = sm         // 8dp
        val itemSpacing = md          // 12dp
    }

    object List {
        val itemPadding = lg          // 16dp
        val itemSpacing = sm          // 8dp
        val sectionSpacing = xl       // 20dp
        val dividerThickness = hairline // 1dp
    }

    object Button {
        val paddingHorizontal = lg    // 16dp
        val paddingVertical = md      // 12dp
        val minHeight = 48.dp
        val iconSpacing = sm          // 8dp
        val cornerRadius = sm         // 8dp
    }

    object Input {
        val paddingHorizontal = lg    // 16dp
        val paddingVertical = md      // 12dp
        val minHeight = 56.dp
        val cornerRadius = sm         // 8dp
    }

    object Chip {
        val paddingHorizontal = md    // 12dp
        val paddingVertical = sm      // 8dp
        val iconSpacing = xs          // 4dp
        val cornerRadius = lg         // 16dp
        val spacing = sm              // 8dp
    }

    object Icon {
        val small = 16.dp
        val medium = 24.dp
        val large = 32.dp
        val extraLarge = 48.dp
    }

    object Avatar {
        val small = 32.dp
        val medium = 40.dp
        val large = 56.dp
        val extraLarge = 72.dp
    }

    object Screen {
        val horizontal = lg           // 16dp
        val vertical = lg             // 16dp
        val top = xxl                 // 24dp
        val bottom = xxl              // 24dp
    }

    object BottomNavigation {
        val height = 80.dp
        val iconSize = medium         // 24dp
        val padding = sm              // 8dp
    }

    object AppBar {
        val height = 64.dp
        val paddingHorizontal = lg    // 16dp
        val elevation = xs            // 4dp
    }

    object Divider {
        val thickness = hairline      // 1dp
        val indent = lg               // 16dp
    }

    // Match-specific spacing
    object Match {
        val cardPadding = lg          // 16dp
        val scorePadding = md         // 12dp
        val teamSpacing = sm          // 8dp
        val timeSpacing = xs          // 4dp
        val logoSize = Icon.large     // 32dp
        val logoSpacing = md          // 12dp
    }

    // League-specific spacing
    object League {
        val headerPadding = lg        // 16dp
        val itemPadding = md          // 12dp
        val logoSize = Icon.medium    // 24dp
        val badgeSize = Icon.small    // 16dp
    }

    // Statistics spacing
    object Stats {
        val barHeight = xs            // 4dp
        val barSpacing = sm           // 8dp
        val labelSpacing = xs         // 4dp
        val sectionSpacing = lg       // 16dp
    }

    // Player/Team spacing
    object Profile {
        val avatarSize = Avatar.large // 56dp
        val headerSpacing = xl        // 20dp
        val sectionSpacing = xxl      // 24dp
        val statItemSpacing = md      // 12dp
    }
}

// Spacing utilities
object SpacingUtils {
    fun getCardSpacing(isCompact: Boolean = false): Dp {
        return if (isCompact) Spacing.md else Spacing.lg
    }

    fun getListItemSpacing(isDense: Boolean = false): Dp {
        return if (isDense) Spacing.sm else Spacing.md
    }

    fun getIconSize(size: String): Dp {
        return when (size.lowercase()) {
            "small", "sm" -> Spacing.Icon.small
            "medium", "md" -> Spacing.Icon.medium
            "large", "lg" -> Spacing.Icon.large
            "xl", "extra_large" -> Spacing.Icon.extraLarge
            else -> Spacing.Icon.medium
        }
    }

    fun getAvatarSize(size: String): Dp {
        return when (size.lowercase()) {
            "small", "sm" -> Spacing.Avatar.small
            "medium", "md" -> Spacing.Avatar.medium
            "large", "lg" -> Spacing.Avatar.large
            "xl", "extra_large" -> Spacing.Avatar.extraLarge
            else -> Spacing.Avatar.medium
        }
    }
}