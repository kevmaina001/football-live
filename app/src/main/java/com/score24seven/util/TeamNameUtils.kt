/*
 * Team Name Utilities
 * Provides functions for abbreviating and formatting team names
 */

package com.score24seven.util

object TeamNameUtils {

    // Common team abbreviations for better display in limited space
    private val TEAM_ABBREVIATIONS = mapOf(
        // Premier League
        "Manchester United" to "Man Utd",
        "Manchester City" to "Man City",
        "Newcastle United" to "Newcastle",
        "Tottenham Hotspur" to "Tottenham",
        "Brighton and Hove Albion" to "Brighton",
        "West Ham United" to "West Ham",
        "Wolverhampton Wanderers" to "Wolves",
        "Nottingham Forest" to "Nott'm Forest",
        "Leicester City" to "Leicester",

        // La Liga
        "Athletic Club" to "Athletic",
        "Atletico Madrid" to "Atlético",
        "Real Sociedad" to "R. Sociedad",
        "Real Betis" to "Betis",
        "Real Madrid" to "Real Madrid",
        "Villarreal CF" to "Villarreal",
        "Rayo Vallecano" to "Rayo",

        // Serie A
        "Inter Milan" to "Inter",
        "AC Milan" to "Milan",
        "Hellas Verona" to "Verona",

        // Bundesliga
        "Borussia Dortmund" to "Dortmund",
        "Borussia Mönchengladbach" to "M'gladbach",
        "Bayern Munich" to "Bayern",
        "RB Leipzig" to "Leipzig",
        "Bayer Leverkusen" to "Leverkusen",
        "Eintracht Frankfurt" to "Frankfurt",

        // Ligue 1
        "Paris Saint-Germain" to "PSG",
        "Paris Saint Germain" to "PSG",
        "Olympique de Marseille" to "Marseille",
        "Olympique Lyonnais" to "Lyon",
        "AS Monaco" to "Monaco",

        // Other common abbreviations
        "FC" to "",
        "Football Club" to ""
    )

    /**
     * Get abbreviated team name for display in standings
     * @param teamName The full team name
     * @param maxLength Maximum length before abbreviation is enforced (default 15)
     * @return Abbreviated team name
     */
    fun getAbbreviatedName(teamName: String, maxLength: Int = 15): String {
        // First check if there's a predefined abbreviation
        TEAM_ABBREVIATIONS[teamName]?.let { abbreviation ->
            if (abbreviation.isNotEmpty()) {
                return abbreviation
            }
        }

        // If the name is already short enough, return as is
        if (teamName.length <= maxLength) {
            return teamName
        }

        // Remove common suffixes
        var abbreviated = teamName
            .replace(" FC", "")
            .replace(" CF", "")
            .replace(" United", "")
            .replace(" City", "")
            .trim()

        // If still too long, use first part only
        if (abbreviated.length > maxLength) {
            val parts = abbreviated.split(" ")
            abbreviated = if (parts.size > 1 && parts[0].length <= maxLength) {
                parts[0]
            } else {
                // Last resort: truncate with ellipsis
                abbreviated.take(maxLength - 1) + "…"
            }
        }

        return abbreviated
    }

    /**
     * Get very short abbreviation for extremely limited space (e.g., 3-4 chars)
     * @param teamName The full team name
     * @return Very short abbreviation (3-4 characters)
     */
    fun getShortCode(teamName: String): String {
        // Check for known abbreviations first
        when (teamName) {
            "Manchester United" -> return "MUN"
            "Manchester City" -> return "MCI"
            "Liverpool" -> return "LIV"
            "Arsenal" -> return "ARS"
            "Chelsea" -> return "CHE"
            "Tottenham Hotspur" -> return "TOT"
            "Newcastle United" -> return "NEW"
            "Brighton and Hove Albion" -> return "BHA"
            "West Ham United" -> return "WHU"
            "Wolverhampton Wanderers" -> return "WOL"
            "Real Madrid" -> return "RMA"
            "Barcelona" -> return "BAR"
            "Atletico Madrid" -> return "ATM"
            "Bayern Munich" -> return "BAY"
            "Borussia Dortmund" -> return "BVB"
            "Paris Saint-Germain", "Paris Saint Germain" -> return "PSG"
            "Inter Milan" -> return "INT"
            "AC Milan" -> return "MIL"
            "Juventus" -> return "JUV"
        }

        // Generate abbreviation from first letters of words
        val words = teamName.split(" ").filter { it.isNotEmpty() }
        return when {
            words.size >= 3 -> {
                // Take first letter of first 3 words
                words.take(3).joinToString("") { it.first().uppercase() }
            }
            words.size == 2 -> {
                // Take first 2 letters of first word + first letter of second word
                words[0].take(2).uppercase() + words[1].first().uppercase()
            }
            else -> {
                // Single word - take first 3 characters
                teamName.take(3).uppercase()
            }
        }
    }
}
