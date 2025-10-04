/*
 * Mock data provider for when API is unavailable
 */

package com.score24seven.data.mock

import com.score24seven.domain.model.*
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockDataProvider @Inject constructor() {

    private var isUsingMockData = false

    fun setUsingMockData(using: Boolean) {
        isUsingMockData = using
    }

    fun isCurrentlyUsingMockData(): Boolean = isUsingMockData

    fun getMockLiveMatches(): List<Match> {
        // Return empty list when there are no live matches
        // This prevents showing dummy data when API is unavailable
        return emptyList()
    }

    fun getMockTodayMatches(): List<Match> {
        val now = LocalDateTime.now()
        return listOf(
            Match(
                id = 2001,
                homeTeam = Team(7, "Tottenham", "TOT", "https://example.com/tot.png", "England", 1882, false),
                awayTeam = Team(8, "Newcastle", "NEW", "https://example.com/new.png", "England", 1892, false),
                league = League(39, "Premier League", "England", "https://example.com/pl.png"),
                fixture = Fixture(now.plusHours(2), "UTC", System.currentTimeMillis()),
                score = Score(home = null, away = null),
                status = MatchStatus("NS", "Not Started", null, false, false),
                venue = Venue(4, "Tottenham Hotspur Stadium", "London")
            ),
            Match(
                id = 2002,
                homeTeam = Team(9, "Bayern Munich", "BAY", "https://example.com/bay.png", "Germany", 1900, false),
                awayTeam = Team(10, "Borussia Dortmund", "DOR", "https://example.com/dor.png", "Germany", 1909, false),
                league = League(78, "Bundesliga", "Germany", "https://example.com/bundesliga.png"),
                fixture = Fixture(now.plusHours(4), "UTC", System.currentTimeMillis()),
                score = Score(home = null, away = null),
                status = MatchStatus("NS", "Not Started", null, false, false),
                venue = Venue(5, "Allianz Arena", "Munich")
            ),
            Match(
                id = 2003,
                homeTeam = Team(11, "AC Milan", "MIL", "https://example.com/mil.png", "Italy", 1899, false),
                awayTeam = Team(12, "Inter Milan", "INT", "https://example.com/int.png", "Italy", 1908, false),
                league = League(135, "Serie A", "Italy", "https://example.com/seriea.png"),
                fixture = Fixture(now.minusHours(1), "UTC", System.currentTimeMillis()),
                score = Score(home = 2, away = 1),
                status = MatchStatus("FT", "Full Time", null, false, true),
                venue = Venue(6, "San Siro", "Milan")
            ),
            Match(
                id = 2004,
                homeTeam = Team(13, "PSG", "PSG", "https://example.com/psg.png", "France", 1970, false),
                awayTeam = Team(14, "Marseille", "MAR", "https://example.com/mar.png", "France", 1899, false),
                league = League(61, "Ligue 1", "France", "https://example.com/ligue1.png"),
                fixture = Fixture(now.plusHours(6), "UTC", System.currentTimeMillis()),
                score = Score(home = null, away = null),
                status = MatchStatus("NS", "Not Started", null, false, false),
                venue = Venue(7, "Parc des Princes", "Paris")
            )
        )
    }

    fun shouldUseMockData(errorCode: Int): Boolean {
        return when (errorCode) {
            403 -> true  // API key invalid
            429 -> true  // Rate limit exceeded
            else -> false
        }
    }

    fun shouldUseMockDataForException(exception: Exception): Boolean {
        val message = exception.message?.lowercase() ?: ""
        return when {
            message.contains("unable to resolve host") -> true
            message.contains("failed to connect") -> true
            message.contains("timeout") -> true
            message.contains("network") -> true
            message.contains("no address associated with hostname") -> true
            else -> false
        }
    }
}