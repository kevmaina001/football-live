/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.model

data class Standing(
    val rank: Int,
    val team: Team,
    val points: Int,
    val goalsDiff: Int,
    val form: String? = null,
    val status: String? = null,
    val description: String? = null,
    val update: String? = null,
    val all: StandingStats,
    val home: StandingStats? = null,
    val away: StandingStats? = null,
    val group: String? = null
)

data class StandingStats(
    val played: Int,
    val win: Int,
    val draw: Int,
    val lose: Int,
    val goals: StandingGoals
)

data class StandingGoals(
    val goalsFor: Int,
    val goalsAgainst: Int
)

fun Standing.getWinPercentage(): Float {
    return if (all.played > 0) (all.win.toFloat() / all.played) * 100 else 0f
}

fun Standing.getFormDisplay(): String {
    return form?.take(5) ?: ""
}

fun Standing.getStatusColor(): String {
    return when (status?.lowercase()) {
        "champions league" -> "#4CAF50"
        "europa league" -> "#FF9800"
        "europa conference league" -> "#FFC107"
        "relegation" -> "#F44336"
        else -> "#9E9E9E"
    }
}