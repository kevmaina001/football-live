/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.util

import com.kickscore.live.domain.model.Match
import com.kickscore.live.ui.state.UiState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// Extension function for UiState to get data or null
fun <T> UiState<T>.dataOrNull(): T? = when (this) {
    is UiState.Success -> data
    else -> null
}

// Extension function to check if match has a score
fun Match.hasScore(): Boolean {
    return score?.home != null && score.away != null
}

// Extension function to get time display for matches
fun Match.getTimeDisplay(): String {
    return when {
        status.isFinished -> "FT"
        status.isLive -> "${status.elapsed ?: 0}'"
        else -> {
            // Format the time for scheduled matches
            fixture.dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        }
    }
}

// Helper to check if match status indicates finished
private val com.kickscore.live.domain.model.MatchStatus.isFinished: Boolean
    get() = short.uppercase() in listOf("FT", "AET", "PEN", "FT_PEN", "CANC", "ABD", "PST")