/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.mapper

import com.score24seven.data.api.*
import com.score24seven.domain.model.*

fun TeamStatisticsDto.toTeamStatistics(): TeamStatistics {
    return TeamStatistics(
        team = team.toDomainTeam(),
        league = league.toDomainLeague(),
        form = form,
        fixtures = fixtures.toFixtureStatistics(),
        goals = goals.toGoalStatistics(),
        biggest = biggest.toBiggestStatistics(),
        cleanSheet = cleanSheet.toCleanSheetStatistics(),
        failedToScore = failedToScore.toFailedToScoreStatistics(),
        penalty = penalty.toPenaltyStatistics(),
        lineups = lineups.map { it.toLineupFormation() },
        cards = cards.toCardStatistics()
    )
}

fun TeamDto.toDomainTeam(): Team {
    return Team(
        id = id,
        name = name,
        code = code,
        logo = logo,
        country = country,
        founded = founded,
        isNational = national,
        isFavorite = false
    )
}

fun LeagueDto.toDomainLeague(): League {
    return League(
        id = id,
        name = name,
        country = country,
        logo = logo,
        flag = flag,
        season = season,
        round = round
    )
}

fun FixtureStatsDto.toFixtureStatistics(): FixtureStatistics {
    return FixtureStatistics(
        played = played.toStatsHomeAway(),
        wins = wins.toStatsHomeAway(),
        draws = draws.toStatsHomeAway(),
        loses = loses.toStatsHomeAway()
    )
}

fun StatsHomeAwayDto.toStatsHomeAway(): StatsHomeAway {
    return StatsHomeAway(
        home = home,
        away = away,
        total = total
    )
}

fun GoalStatsDto.toGoalStatistics(): GoalStatistics {
    return GoalStatistics(
        goalsFor = goalsFor.toStatsHomeAway(),
        goalsAgainst = goalsAgainst.toStatsHomeAway()
    )
}

fun BiggestStatsDto.toBiggestStatistics(): BiggestStatistics {
    return BiggestStatistics(
        streak = streak.toStreak(),
        wins = wins.toWins(),
        loses = loses.toLoses(),
        goals = goals.toGoals()
    )
}

fun StreakDto.toStreak(): Streak {
    return Streak(
        wins = wins,
        draws = draws,
        loses = loses
    )
}

fun WinsDto.toWins(): Wins {
    return Wins(
        home = home,
        away = away
    )
}

fun LosesDto.toLoses(): Loses {
    return Loses(
        home = home,
        away = away
    )
}

fun GoalsDto.toGoals(): Goals {
    return Goals(
        goalsFor = goalsFor.toStatsHomeAway(),
        goalsAgainst = goalsAgainst.toStatsHomeAway()
    )
}

fun CleanSheetStatsDto.toCleanSheetStatistics(): CleanSheetStatistics {
    return CleanSheetStatistics(
        home = home,
        away = away,
        total = total
    )
}

fun FailedToScoreStatsDto.toFailedToScoreStatistics(): FailedToScoreStatistics {
    return FailedToScoreStatistics(
        home = home,
        away = away,
        total = total
    )
}

fun PenaltyStatsDto.toPenaltyStatistics(): PenaltyStatistics {
    return PenaltyStatistics(
        scored = scored.toPenaltyScored(),
        missed = missed.toPenaltyScored(),
        total = total
    )
}

fun PenaltyScoredDto.toPenaltyScored(): PenaltyScored {
    return PenaltyScored(
        total = total,
        percentage = percentage
    )
}

fun LineupFormationDto.toLineupFormation(): LineupFormation {
    return LineupFormation(
        formation = formation,
        played = played
    )
}

fun CardStatsDto.toCardStatistics(): CardStatistics {
    return CardStatistics(
        yellow = yellow.toStatsTime(),
        red = red.toStatsTime()
    )
}

fun StatsTimeDto.toStatsTime(): StatsTime {
    return StatsTime(
        range0_15 = `0-15`?.toStatsTimeRange(),
        range16_30 = `16-30`?.toStatsTimeRange(),
        range31_45 = `31-45`?.toStatsTimeRange(),
        range46_60 = `46-60`?.toStatsTimeRange(),
        range61_75 = `61-75`?.toStatsTimeRange(),
        range76_90 = `76-90`?.toStatsTimeRange(),
        range91_105 = `91-105`?.toStatsTimeRange(),
        range106_120 = `106-120`?.toStatsTimeRange()
    )
}

fun StatsTimeRangeDto.toStatsTimeRange(): StatsTimeRange {
    return StatsTimeRange(
        total = total,
        percentage = percentage
    )
}