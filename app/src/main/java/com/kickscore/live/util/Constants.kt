/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.util

object Constants {
    // API Configuration
    const val RAPIDAPI_HOST = "api-football-v1.p.rapidapi.com"
    const val RAPIDAPI_BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/"

    // RapidAPI Key - Using a working key for Football API
    const val RAPIDAPI_KEY = "3bdb371035msh4a88eff8edf2ec5p103690jsnd48d0c24e31b"

    // League IDs for Featured Leagues
    const val PREMIER_LEAGUE_ID = 39
    const val CHAMPIONS_LEAGUE_ID = 2
    const val LA_LIGA_ID = 140
    const val SERIE_A_ID = 135
    const val BUNDESLIGA_ID = 78
    const val LIGUE_1_ID = 61
    const val EUROPA_LEAGUE_ID = 3

    // Current Season
    const val CURRENT_SEASON = 2024

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val PREFETCH_DISTANCE = 5

    // Cache
    const val CACHE_TIMEOUT_MINUTES = 5
    const val LIVE_DATA_REFRESH_INTERVAL = 30_000L // 30 seconds

    // WebSocket
    const val WEBSOCKET_URL = "wss://api-football-v1.p.rapidapi.com/v3/websocket"
    const val WEBSOCKET_RECONNECT_INTERVAL = 5000L // 5 seconds

    // Date Formats
    const val API_DATE_FORMAT = "yyyy-MM-dd"
    const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy"
    const val DISPLAY_TIME_FORMAT = "HH:mm"
}