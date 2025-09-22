/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.util

object Config {
    const val API_KEY = "3bdb371035msh4a88eff8edf2ec5p103690jsnd48d0c24e31b"

    // Priority leagues with major competitions always shown
    val PRIORITY_LEAGUE_IDS = intArrayOf(
        39,  // Premier League (England) - Arsenal, Chelsea, Manchester United, etc.
        2,   // UEFA Champions League
        3,   // UEFA Europa League
        1,   // World Cup (FIFA)
        140, // La Liga (Spain)
        78,  // Bundesliga (Germany)
        135, // Serie A (Italy)
        61,  // Ligue 1 (France)
        5,   // UEFA Nations League
        848, // UEFA Europa Conference League
        45,  // FA Cup (England)
        40,  // Championship (England)
        4,   // European Championship (UEFA Euro)
        253  // Major League Soccer
    )

    // Static favorite league info (always show these regardless of matches)
    val FAVORITE_LEAGUE_INFO = mapOf(
        39 to Pair("Premier League", "England"),
        2 to Pair("UEFA Champions League", "Europe"),
        3 to Pair("UEFA Europa League", "Europe"),
        1 to Pair("World Cup", "FIFA"),
        140 to Pair("La Liga", "Spain"),
        78 to Pair("Bundesliga", "Germany"),
        135 to Pair("Serie A", "Italy"),
        61 to Pair("Ligue 1", "France"),
        5 to Pair("UEFA Nations League", "Europe"),
        848 to Pair("UEFA Europa Conference League", "Europe"),
        45 to Pair("FA Cup", "England"),
        40 to Pair("Championship", "England"),
        4 to Pair("European Championship", "Europe"),
        253 to Pair("Major League Soccer", "USA")
    )

    const val RAPIDAPI_HOST = "api-football-v1.p.rapidapi.com"
}