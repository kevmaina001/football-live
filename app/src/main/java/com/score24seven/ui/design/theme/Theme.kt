/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.score24seven.ui.design.tokens.Colors
import com.score24seven.ui.design.tokens.Typography

// Dark color scheme (primary theme)
private val DarkColorScheme = darkColorScheme(
    primary = Colors.Primary,
    onPrimary = Colors.OnPrimary,
    primaryContainer = Colors.PrimaryVariant,
    onPrimaryContainer = Colors.OnPrimary,

    secondary = Colors.Accent,
    onSecondary = Colors.OnAccent,
    secondaryContainer = Colors.AccentVariant,
    onSecondaryContainer = Colors.OnAccent,

    tertiary = Colors.Success,
    onTertiary = Colors.OnPrimary,

    error = Colors.Danger,
    onError = Colors.OnPrimary,

    background = Colors.Background,
    onBackground = Colors.OnBackground,

    surface = Colors.Surface,
    onSurface = Colors.OnSurface,
    surfaceVariant = Colors.SurfaceVariant,
    onSurfaceVariant = Colors.TextSecondary,

    outline = Colors.Border,
    outlineVariant = Colors.Divider,

    inverseSurface = Colors.Light.Surface,
    inverseOnSurface = Colors.Light.OnSurface,
    inversePrimary = Colors.Primary,

    surfaceTint = Colors.Primary,
    scrim = Colors.Overlay
)

// Light color scheme (optional)
private val LightColorScheme = lightColorScheme(
    primary = Colors.Primary,
    onPrimary = Colors.OnPrimary,
    primaryContainer = Colors.PrimaryVariant,
    onPrimaryContainer = Colors.OnPrimary,

    secondary = Colors.Accent,
    onSecondary = Colors.OnAccent,
    secondaryContainer = Colors.AccentVariant,
    onSecondaryContainer = Colors.OnAccent,

    tertiary = Colors.Success,
    onTertiary = Colors.OnPrimary,

    error = Colors.Danger,
    onError = Colors.OnPrimary,

    background = Colors.Light.Background,
    onBackground = Colors.Light.OnBackground,

    surface = Colors.Light.Surface,
    onSurface = Colors.Light.OnSurface,
    surfaceVariant = Colors.Light.SurfaceVariant,
    onSurfaceVariant = Colors.Light.TextSecondary,

    outline = Colors.Light.Border,
    outlineVariant = Colors.Light.Divider,

    inverseSurface = Colors.Surface,
    inverseOnSurface = Colors.OnSurface,
    inversePrimary = Colors.Primary,

    surfaceTint = Colors.Primary,
    scrim = Colors.Overlay
)

// Custom colors not covered by Material 3
data class ExtendedColors(
    val success: androidx.compose.ui.graphics.Color = Colors.Success,
    val warning: androidx.compose.ui.graphics.Color = Colors.Warning,
    val info: androidx.compose.ui.graphics.Color = Colors.Info,
    val liveIndicator: androidx.compose.ui.graphics.Color = Colors.LiveIndicator,
    val scoreHome: androidx.compose.ui.graphics.Color = Colors.ScoreHome,
    val scoreAway: androidx.compose.ui.graphics.Color = Colors.ScoreAway,
    val yellowCard: androidx.compose.ui.graphics.Color = Colors.YellowCard,
    val redCard: androidx.compose.ui.graphics.Color = Colors.RedCard,
    val winColor: androidx.compose.ui.graphics.Color = Colors.WinColor,
    val lossColor: androidx.compose.ui.graphics.Color = Colors.LossColor,
    val drawColor: androidx.compose.ui.graphics.Color = Colors.DrawColor,
    val textTertiary: androidx.compose.ui.graphics.Color = Colors.TextTertiary,
    val shimmer: androidx.compose.ui.graphics.Color = Colors.Shimmer,
    val gradientStart: androidx.compose.ui.graphics.Color = Colors.GradientStart,
    val gradientEnd: androidx.compose.ui.graphics.Color = Colors.GradientEnd
)

val LocalExtendedColors = staticCompositionLocalOf { ExtendedColors() }

@Composable
fun Score24SevenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val extendedColors = ExtendedColors()

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Extension to access custom colors from MaterialTheme
val MaterialTheme.extendedColors: ExtendedColors
    @Composable get() = LocalExtendedColors.current