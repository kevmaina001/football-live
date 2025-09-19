/*
 * League-specific date context caching for End-to-End League System Flow
 * Maintains date selection state when navigating within league context
 */

package com.kickscore.live.ui.navigation

object LeagueDateCache {
    private val leagueDateMap = mutableMapOf<Int, String>() // leagueId -> dateContext (yyyy-MM-dd)

    fun setLeagueDate(leagueId: Int, dateContext: String) {
        leagueDateMap[leagueId] = dateContext
        println("DEBUG: LeagueDateCache - Set date context for league $leagueId: $dateContext")
    }

    fun getLeagueDate(leagueId: Int): String? {
        val dateContext = leagueDateMap[leagueId]
        println("DEBUG: LeagueDateCache - Retrieved date context for league $leagueId: $dateContext")
        return dateContext
    }

    fun clearLeagueDate(leagueId: Int) {
        leagueDateMap.remove(leagueId)
        println("DEBUG: LeagueDateCache - Cleared date context for league $leagueId")
    }

    fun clearAllDates() {
        leagueDateMap.clear()
        println("DEBUG: LeagueDateCache - Cleared all league date contexts")
    }

    // Debug method to see all cached dates
    fun getAllCachedDates(): Map<Int, String> {
        return leagueDateMap.toMap()
    }
}