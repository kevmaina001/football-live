/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.model

data class MockMatch(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val status: String,
    val time: String,
    val league: String = "",
    val isLive: Boolean = false
)