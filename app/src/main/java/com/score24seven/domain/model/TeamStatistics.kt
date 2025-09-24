/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.model

data class TeamStatistics(
    val team: Team,
    val league: League,
    val form: String = "",
    val fixtures: FixtureStatistics,
    val goals: GoalStatistics,
    val biggest: BiggestStatistics,
    val cleanSheet: CleanSheetStatistics,
    val failedToScore: FailedToScoreStatistics,
    val penalty: PenaltyStatistics,
    val lineups: List<LineupFormation>,
    val cards: CardStatistics
)

data class FixtureStatistics(
    val played: StatsHomeAway,
    val wins: StatsHomeAway,
    val draws: StatsHomeAway,
    val loses: StatsHomeAway
)

data class StatsHomeAway(
    val home: Int?,
    val away: Int?,
    val total: Int?
)

data class GoalStatistics(
    val goalsFor: StatsHomeAway,
    val goalsAgainst: StatsHomeAway
)

data class BiggestStatistics(
    val streak: Streak,
    val wins: Wins,
    val loses: Loses,
    val goals: Goals
)

data class Streak(
    val wins: Int?,
    val draws: Int?,
    val loses: Int?
)

data class Wins(
    val home: String?,
    val away: String?
)

data class Loses(
    val home: String?,
    val away: String?
)

data class Goals(
    val goalsFor: StatsHomeAway,
    val goalsAgainst: StatsHomeAway
)

data class CleanSheetStatistics(
    val home: Int?,
    val away: Int?,
    val total: Int?
)

data class FailedToScoreStatistics(
    val home: Int?,
    val away: Int?,
    val total: Int?
)

data class PenaltyStatistics(
    val scored: PenaltyScored,
    val missed: PenaltyScored,
    val total: Int?
)

data class PenaltyScored(
    val total: Int?,
    val percentage: String?
)

data class LineupFormation(
    val formation: String,
    val played: Int
)

data class CardStatistics(
    val yellow: StatsTime,
    val red: StatsTime
)

data class StatsTime(
    val range0_15: StatsTimeRange?,
    val range16_30: StatsTimeRange?,
    val range31_45: StatsTimeRange?,
    val range46_60: StatsTimeRange?,
    val range61_75: StatsTimeRange?,
    val range76_90: StatsTimeRange?,
    val range91_105: StatsTimeRange?,
    val range106_120: StatsTimeRange?
)

data class StatsTimeRange(
    val total: Int?,
    val percentage: String?
)