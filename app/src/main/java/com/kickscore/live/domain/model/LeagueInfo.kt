/*
 * League domain models based on friend's working implementation
 */

package com.kickscore.live.domain.model

data class LeagueInfo(
    val league: LeagueDetails,
    val country: CountryDetails,
    val seasons: List<SeasonDetails>
) {
    fun getCurrentSeason(): SeasonDetails? {
        return seasons.find { it.current } ?: seasons.maxByOrNull { it.year }
    }

    fun getLatestYear(): Int {
        return getCurrentSeason()?.year ?: seasons.maxOfOrNull { it.year } ?: 2024
    }
}

data class LeagueDetails(
    val id: Int,
    val name: String,
    val type: String,
    val logo: String?
)

data class CountryDetails(
    val name: String,
    val code: String?,
    val flag: String?
)

data class SeasonDetails(
    val year: Int,
    val start: String,
    val end: String,
    val current: Boolean,
    val coverage: CoverageDetails?
)

data class CoverageDetails(
    val standings: Boolean,
    val topScorers: Boolean,
    val predictions: Boolean,
    val fixtures: Boolean
)