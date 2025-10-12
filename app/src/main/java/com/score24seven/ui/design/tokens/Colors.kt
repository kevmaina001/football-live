/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.design.tokens

import androidx.compose.ui.graphics.Color

object Colors {
    // Primary Colors
    val Primary = Color(0xFF12A150)           // Kick Green
    val PrimaryVariant = Color(0xFF0E8142)    // Darker Kick Green
    val OnPrimary = Color(0xFFFFFFFF)         // White

    // Accent Colors
    val Accent = Color(0xFF2E7BF6)            // Electric Blue
    val AccentVariant = Color(0xFF1E5BC5)     // Darker Electric Blue
    val OnAccent = Color(0xFFFFFFFF)          // White

    // Semantic Colors
    val Success = Color(0xFF16A34A)           // Success Green
    val Danger = Color(0xFFDC2626)            // Danger Red
    val Warning = Color(0xFFD97706)           // Warning Orange
    val Info = Color(0xFF2563EB)              // Info Blue

    // Background Colors - Dark Theme First
    val Background = Color(0xFF0C1117)        // Deep Dark Blue
    val Surface = Color(0xFF111827)           // Dark Surface
    val SurfaceVariant = Color(0xFF1F2937)    // Elevated Surface
    val OnBackground = Color(0xFFE5E7EB)      // Light Text on Dark
    val OnSurface = Color(0xFFE5E7EB)         // Light Text on Dark Surface

    // Text Colors
    val TextPrimary = Color(0xFFE5E7EB)       // Primary Text Light
    val TextSecondary = Color(0xFF9CA3AF)     // Secondary Text Medium
    val TextTertiary = Color(0xFF6B7280)      // Tertiary Text Dim
    val TextInverse = Color(0xFF111827)       // Dark text for light backgrounds

    // Divider and Border Colors
    val Divider = Color(0xFF1F2937)           // Subtle dividers
    val Border = Color(0xFF374151)            // Input borders
    val BorderFocused = Color(0xFF60A5FA)     // Focused state

    // Score and Match Status Colors
    val ScoreHome = Color(0xFF12A150)         // Home team score
    val ScoreAway = Color(0xFF2E7BF6)         // Away team score
    val ScoreDraw = Color(0xFF6B7280)         // Draw indicator
    val LiveIndicator = Color(0xFFEF4444)     // Live match indicator
    val MatchFinished = Color(0xFF10B981)     // Finished match
    val MatchScheduled = Color(0xFF8B5CF6)    // Scheduled match

    // Card Colors (Yellow/Red Cards)
    val YellowCard = Color(0xFFFBBF24)        // Yellow card
    val RedCard = Color(0xFFEF4444)           // Red card

    // Team Performance Colors
    val WinColor = Color(0xFF10B981)          // Win indicator
    val LossColor = Color(0xFFEF4444)         // Loss indicator
    val DrawColor = Color(0xFF6B7280)         // Draw indicator

    // League and Competition Colors
    val ChampionsLeague = Color(0xFF003087)   // Champions League Blue
    val EuropaLeague = Color(0xFFFF6900)      // Europa League Orange
    val PremierLeague = Color(0xFF3D195B)     // Premier League Purple
    val LaLiga = Color(0xFFFF6900)           // La Liga Orange
    val SerieA = Color(0xFF008FD7)           // Serie A Blue
    val Bundesliga = Color(0xFFD20515)       // Bundesliga Red

    // Light Theme Colors - Enhanced for better UI
    object Light {
        val Background = Color(0xFFF8FAFC)          // Soft off-white background
        val Surface = Color(0xFFFFFFFF)             // Pure white surface
        val SurfaceVariant = Color(0xFFF1F5F9)      // Light blue-gray variant
        val OnBackground = Color(0xFF0F172A)        // Deep slate text
        val OnSurface = Color(0xFF1E293B)           // Slate text
        val TextPrimary = Color(0xFF0F172A)         // Deep slate for primary text
        val TextSecondary = Color(0xFF64748B)       // Medium slate for secondary
        val TextTertiary = Color(0xFF94A3B8)        // Light slate for tertiary
        val Divider = Color(0xFFE2E8F0)             // Subtle divider
        val Border = Color(0xFFCBD5E1)              // Soft border
    }

    // Gradient Colors
    val GradientStart = Color(0xFF12A150)
    val GradientEnd = Color(0xFF2E7BF6)

    // Overlay Colors
    val Overlay = Color(0x80000000)           // 50% black overlay
    val OverlayLight = Color(0x40000000)      // 25% black overlay
    val OverlayHeavy = Color(0xBF000000)      // 75% black overlay

    // Special Effect Colors
    val Shimmer = Color(0xFF374151)           // Shimmer loading effect
    val Ripple = Color(0x1FFFFFFF)            // Ripple effect on dark
    val Selection = Color(0x2012A150)         // Selection highlight
}

// Color utilities
object ColorUtils {
    fun getTeamPerformanceColor(result: String): Color {
        return when (result.uppercase()) {
            "W", "WIN" -> Colors.WinColor
            "L", "LOSS", "LOSE" -> Colors.LossColor
            "D", "DRAW" -> Colors.DrawColor
            else -> Colors.TextSecondary
        }
    }

    fun getMatchStatusColor(status: String): Color {
        return when (status.uppercase()) {
            "LIVE", "1H", "2H", "HT" -> Colors.LiveIndicator
            "FT", "FINISHED" -> Colors.MatchFinished
            "SCHEDULED", "NS" -> Colors.MatchScheduled
            else -> Colors.TextSecondary
        }
    }

    fun getLeagueColor(leagueName: String): Color {
        return when {
            leagueName.contains("Champions League", ignoreCase = true) -> Colors.ChampionsLeague
            leagueName.contains("Europa League", ignoreCase = true) -> Colors.EuropaLeague
            leagueName.contains("Premier League", ignoreCase = true) -> Colors.PremierLeague
            leagueName.contains("La Liga", ignoreCase = true) -> Colors.LaLiga
            leagueName.contains("Serie A", ignoreCase = true) -> Colors.SerieA
            leagueName.contains("Bundesliga", ignoreCase = true) -> Colors.Bundesliga
            else -> Colors.Primary
        }
    }
}