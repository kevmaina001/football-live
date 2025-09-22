/*
 * Cache for maintaining date selection state between competitions screens
 * Ensures connected navigation flow between parent and child screens
 */

package com.score24seven.ui.navigation

object CompetitionDateCache {
    private var selectedDateIndex: Int? = null

    fun setDateIndex(dateIndex: Int) {
        selectedDateIndex = dateIndex
        println("DEBUG: CompetitionDateCache - Date index set to: $dateIndex")
    }

    fun getDateIndex(): Int {
        val index = selectedDateIndex ?: 2 // Default to today if not set
        println("DEBUG: CompetitionDateCache - Retrieved date index: $index")
        return index
    }

    fun clearDateIndex() {
        selectedDateIndex = null
        println("DEBUG: CompetitionDateCache - Date index cleared")
    }
}