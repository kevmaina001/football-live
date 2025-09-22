/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.navigation

import com.score24seven.domain.model.Match

/**
 * Simple in-memory cache for passing Match objects between screens
 * This avoids serialization issues with complex objects in navigation
 */
object MatchCache {
    private var cachedMatch: Match? = null

    fun setMatch(match: Match) {
        try {
            println("DEBUG: MatchCache - Setting match: ${match.homeTeam.name} vs ${match.awayTeam.name}")
            println("DEBUG: MatchCache - Match ID: ${match.id}")
            cachedMatch = match
        } catch (e: Exception) {
            println("DEBUG: MatchCache - Error setting match: ${e.message}")
            cachedMatch = null
        }
    }

    fun getMatch(): Match? {
        println("DEBUG: MatchCache - Getting match: ${cachedMatch?.let { "${it.homeTeam.name} vs ${it.awayTeam.name}" } ?: "null"}")
        return cachedMatch
    }

    fun clearMatch() {
        println("DEBUG: MatchCache - Clearing match")
        cachedMatch = null
    }
}