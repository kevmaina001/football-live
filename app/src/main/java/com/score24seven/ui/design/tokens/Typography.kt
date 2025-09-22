/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.design.tokens

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
// import com.score24seven.R  // Placeholder - fonts will fallback to default

// Inter Font Family - Google Fonts (using default fonts as fallback)
val InterFontFamily = FontFamily.Default

// Roboto Fallback (since Inter might not be available)
val RobotoFontFamily = FontFamily.Default

// Primary font family with fallback
val PrimaryFontFamily = FontFamily.Default

object Score24SevenTypography {

    // Display styles - for large hero text
    val displayLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    )

    val displayMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp
    )

    val displaySmall = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )

    // Headline styles - for section headers
    val headlineLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )

    val headlineMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )

    val headlineSmall = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )

    // Title styles - for card titles and important text
    val titleLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    )

    val titleMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    val titleSmall = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    )

    // Body styles - for main content
    val bodyLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    )

    val bodyMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )

    val bodySmall = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )

    // Label styles - for buttons and labels
    val labelLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    val labelMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

    val labelSmall = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )

    // Custom styles for specific use cases
    val scoreDisplay = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.5).sp
    )

    val scoreDisplayLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.5).sp
    )

    val teamName = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )

    val matchTime = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

    val leagueName = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp
    )

    val statValue = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    )

    val statLabel = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp
    )

    val chipText = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    )

    val buttonText = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    val captionEmphasis = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
}

// Material 3 Typography instance
val Typography = Typography(
    displayLarge = Score24SevenTypography.displayLarge,
    displayMedium = Score24SevenTypography.displayMedium,
    displaySmall = Score24SevenTypography.displaySmall,
    headlineLarge = Score24SevenTypography.headlineLarge,
    headlineMedium = Score24SevenTypography.headlineMedium,
    headlineSmall = Score24SevenTypography.headlineSmall,
    titleLarge = Score24SevenTypography.titleLarge,
    titleMedium = Score24SevenTypography.titleMedium,
    titleSmall = Score24SevenTypography.titleSmall,
    bodyLarge = Score24SevenTypography.bodyLarge,
    bodyMedium = Score24SevenTypography.bodyMedium,
    bodySmall = Score24SevenTypography.bodySmall,
    labelLarge = Score24SevenTypography.labelLarge,
    labelMedium = Score24SevenTypography.labelMedium,
    labelSmall = Score24SevenTypography.labelSmall
)

// Typography utilities
object TypographyUtils {
    fun getScoreTextStyle(isLarge: Boolean = false): TextStyle {
        return if (isLarge) Score24SevenTypography.scoreDisplayLarge
               else Score24SevenTypography.scoreDisplay
    }

    fun getTeamTextStyle(isSelected: Boolean = false): TextStyle {
        return Score24SevenTypography.teamName.copy(
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
        )
    }

    fun getStatTextStyle(isEmphasis: Boolean = false): TextStyle {
        return if (isEmphasis) Score24SevenTypography.statValue
               else Score24SevenTypography.statLabel
    }
}