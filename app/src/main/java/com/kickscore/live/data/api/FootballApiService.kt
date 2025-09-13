/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.api

import com.kickscore.live.data.dto.ApiResponse
import com.kickscore.live.data.dto.LeagueResponse
import com.kickscore.live.data.dto.MatchDto
import com.kickscore.live.data.dto.MatchResponse
import com.kickscore.live.data.dto.StandingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballApiService {

    companion object {
        const val BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/"
    }

    // Live matches
    @GET("fixtures")
    suspend fun getLiveMatches(
        @Query("live") live: String = "all",
        @Query("timezone") timezone: String = "UTC"
    ): Response<ApiResponse<List<MatchDto>>>

    // Today's fixtures
    @GET("fixtures")
    suspend fun getTodayFixtures(
        @Query("date") date: String,
        @Query("timezone") timezone: String = "UTC"
    ): Response<ApiResponse<List<MatchDto>>>

    // Fixtures by date range
    @GET("fixtures")
    suspend fun getFixturesByDateRange(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("timezone") timezone: String = "UTC"
    ): Response<ApiResponse<List<MatchDto>>>

    // League fixtures
    @GET("fixtures")
    suspend fun getLeagueFixtures(
        @Query("league") leagueId: Int,
        @Query("season") season: Int,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null
    ): Response<ApiResponse<List<MatchDto>>>

    // Team fixtures
    @GET("fixtures")
    suspend fun getTeamFixtures(
        @Query("team") teamId: Int,
        @Query("season") season: Int,
        @Query("last") last: Int? = null,
        @Query("next") next: Int? = null
    ): Response<ApiResponse<List<MatchDto>>>

    // Match details
    @GET("fixtures")
    suspend fun getMatchDetails(
        @Query("id") fixtureId: Int,
        @Query("timezone") timezone: String = "UTC"
    ): Response<ApiResponse<List<MatchDto>>>

    // Head to head
    @GET("fixtures/headtohead")
    suspend fun getHeadToHead(
        @Query("h2h") teams: String, // format: "teamId1-teamId2"
        @Query("last") last: Int = 10
    ): Response<ApiResponse<List<MatchDto>>>

    // Leagues
    @GET("leagues")
    suspend fun getLeagues(
        @Query("country") country: String? = null,
        @Query("season") season: Int? = null,
        @Query("current") current: Boolean = true
    ): Response<ApiResponse<LeagueResponse>>

    // League standings
    @GET("standings")
    suspend fun getLeagueStandings(
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<ApiResponse<List<StandingResponse>>>

    // Teams
    @GET("teams")
    suspend fun getTeams(
        @Query("league") leagueId: Int? = null,
        @Query("season") season: Int? = null,
        @Query("search") search: String? = null
    ): Response<ApiResponse<List<TeamDto>>>

    // Team statistics
    @GET("teams/statistics")
    suspend fun getTeamStatistics(
        @Query("league") leagueId: Int,
        @Query("season") season: Int,
        @Query("team") teamId: Int
    ): Response<ApiResponse<TeamStatisticsDto>>

    // Players
    @GET("players")
    suspend fun getPlayers(
        @Query("team") teamId: Int? = null,
        @Query("league") leagueId: Int? = null,
        @Query("season") season: Int,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1
    ): Response<ApiResponse<List<PlayerDto>>>

    // Top scorers
    @GET("players/topscorers")
    suspend fun getTopScorers(
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<ApiResponse<List<PlayerDto>>>

    // Match events
    @GET("fixtures/events")
    suspend fun getMatchEvents(
        @Query("fixture") fixtureId: Int
    ): Response<ApiResponse<List<EventDto>>>

    // Match lineups
    @GET("fixtures/lineups")
    suspend fun getMatchLineups(
        @Query("fixture") fixtureId: Int
    ): Response<ApiResponse<List<LineupDto>>>

    // Match statistics
    @GET("fixtures/statistics")
    suspend fun getMatchStatistics(
        @Query("fixture") fixtureId: Int
    ): Response<ApiResponse<List<StatisticsDto>>>

    // Predictions
    @GET("predictions")
    suspend fun getMatchPredictions(
        @Query("fixture") fixtureId: Int
    ): Response<ApiResponse<List<PredictionDto>>>
}

// Additional DTOs for API responses
data class TeamDto(
    val id: Int,
    val name: String,
    val code: String?,
    val country: String?,
    val founded: Int?,
    val national: Boolean,
    val logo: String?
)

data class TeamStatisticsDto(
    val league: LeagueDto,
    val team: TeamDto,
    val form: String,
    val fixtures: FixtureStatsDto,
    val goals: GoalStatsDto,
    val biggest: BiggestStatsDto,
    val cleanSheet: CleanSheetStatsDto,
    val failedToScore: FailedToScoreStatsDto,
    val penalty: PenaltyStatsDto,
    val lineups: List<LineupFormationDto>,
    val cards: CardStatsDto
)

data class FixtureStatsDto(
    val played: StatsHomeAwayDto,
    val wins: StatsHomeAwayDto,
    val draws: StatsHomeAwayDto,
    val loses: StatsHomeAwayDto
)

data class StatsHomeAwayDto(
    val home: Int?,
    val away: Int?,
    val total: Int?
)

data class GoalStatsDto(
    val goalsFor: StatsHomeAwayDto,
    val goalsAgainst: StatsHomeAwayDto
)

data class BiggestStatsDto(
    val streak: StreakDto,
    val wins: WinsDto,
    val loses: LosesDto,
    val goals: GoalsDto
)

data class StreakDto(
    val wins: Int?,
    val draws: Int?,
    val loses: Int?
)

data class WinsDto(
    val home: String?,
    val away: String?
)

data class LosesDto(
    val home: String?,
    val away: String?
)

data class GoalsDto(
    val goalsFor: StatsHomeAwayDto,
    val goalsAgainst: StatsHomeAwayDto
)

data class CleanSheetStatsDto(
    val home: Int?,
    val away: Int?,
    val total: Int?
)

data class FailedToScoreStatsDto(
    val home: Int?,
    val away: Int?,
    val total: Int?
)

data class PenaltyStatsDto(
    val scored: PenaltyScoredDto,
    val missed: PenaltyScoredDto,
    val total: Int?
)

data class PenaltyScoredDto(
    val total: Int?,
    val percentage: String?
)

data class LineupFormationDto(
    val formation: String,
    val played: Int
)

data class CardStatsDto(
    val yellow: StatsTimeDto,
    val red: StatsTimeDto
)

data class StatsTimeDto(
    val `0-15`: StatsTimeRangeDto?,
    val `16-30`: StatsTimeRangeDto?,
    val `31-45`: StatsTimeRangeDto?,
    val `46-60`: StatsTimeRangeDto?,
    val `61-75`: StatsTimeRangeDto?,
    val `76-90`: StatsTimeRangeDto?,
    val `91-105`: StatsTimeRangeDto?,
    val `106-120`: StatsTimeRangeDto?
)

data class StatsTimeRangeDto(
    val total: Int?,
    val percentage: String?
)

data class PlayerDto(
    val id: Int,
    val name: String,
    val firstname: String?,
    val lastname: String?,
    val age: Int?,
    val birth: BirthDto?,
    val nationality: String?,
    val height: String?,
    val weight: String?,
    val injured: Boolean?,
    val photo: String?,
    val statistics: List<PlayerStatisticsDto>?
)

data class BirthDto(
    val date: String?,
    val place: String?,
    val country: String?
)

data class PlayerStatisticsDto(
    val team: TeamDto,
    val league: LeagueDto,
    val games: PlayerGamesDto,
    val substitutes: PlayerSubstitutesDto,
    val shots: PlayerShotsDto,
    val goals: PlayerGoalsDto,
    val passes: PlayerPassesDto,
    val tackles: PlayerTacklesDto,
    val duels: PlayerDuelsDto,
    val dribbles: PlayerDribblesDto,
    val fouls: PlayerFoulsDto,
    val cards: PlayerCardsDto,
    val penalty: PlayerPenaltyDto
)

data class PlayerGamesDto(
    val appearences: Int?,
    val lineups: Int?,
    val minutes: Int?,
    val number: Int?,
    val position: String?,
    val rating: String?,
    val captain: Boolean?
)

data class PlayerSubstitutesDto(
    val `in`: Int?,
    val out: Int?,
    val bench: Int?
)

data class PlayerShotsDto(
    val total: Int?,
    val on: Int?
)

data class PlayerGoalsDto(
    val total: Int?,
    val conceded: Int?,
    val assists: Int?,
    val saves: Int?
)

data class PlayerPassesDto(
    val total: Int?,
    val key: Int?,
    val accuracy: Int?
)

data class PlayerTacklesDto(
    val total: Int?,
    val blocks: Int?,
    val interceptions: Int?
)

data class PlayerDuelsDto(
    val total: Int?,
    val won: Int?
)

data class PlayerDribblesDto(
    val attempts: Int?,
    val success: Int?,
    val past: Int?
)

data class PlayerFoulsDto(
    val drawn: Int?,
    val committed: Int?
)

data class PlayerCardsDto(
    val yellow: Int?,
    val yellowred: Int?,
    val red: Int?
)

data class PlayerPenaltyDto(
    val won: Int?,
    val commited: Int?,
    val scored: Int?,
    val missed: Int?,
    val saved: Int?
)

data class EventDto(
    val time: TimeDto,
    val team: TeamDto,
    val player: PlayerDto,
    val assist: PlayerDto?,
    val type: String,
    val detail: String,
    val comments: String?
)

data class TimeDto(
    val elapsed: Int,
    val extra: Int?
)

data class LineupDto(
    val team: TeamDto,
    val coach: CoachDto,
    val formation: String,
    val startXI: List<PlayerPositionDto>,
    val substitutes: List<PlayerPositionDto>
)

data class CoachDto(
    val id: Int,
    val name: String,
    val photo: String?
)

data class PlayerPositionDto(
    val player: PlayerDetailDto
)

data class PlayerDetailDto(
    val id: Int,
    val name: String,
    val number: Int,
    val pos: String,
    val grid: String?
)

data class StatisticsDto(
    val team: TeamDto,
    val statistics: List<StatisticDto>
)

data class StatisticDto(
    val type: String,
    val value: String?
)

data class PredictionDto(
    val predictions: PredictionsDto,
    val league: LeagueDto,
    val teams: TeamsDto,
    val comparison: ComparisonDto,
    val h2h: List<MatchDto>
)

data class PredictionsDto(
    val winner: WinnerPredictionDto?,
    val winOrDraw: Boolean?,
    val underOver: String?,
    val goals: GoalsPredictionDto?,
    val advice: String?,
    val percent: PercentDto?
)

data class WinnerPredictionDto(
    val id: Int?,
    val name: String?,
    val comment: String?
)

data class GoalsPredictionDto(
    val home: String?,
    val away: String?
)

data class PercentDto(
    val home: String?,
    val draw: String?,
    val away: String?
)

data class ComparisonDto(
    val form: ComparisonItemDto,
    val att: ComparisonItemDto,
    val def: ComparisonItemDto,
    val poissonDistribution: ComparisonItemDto,
    val h2h: ComparisonItemDto,
    val goals: ComparisonItemDto,
    val total: ComparisonItemDto
)

data class ComparisonItemDto(
    val home: String?,
    val away: String?
)

data class TeamsDto(
    val home: TeamDto,
    val away: TeamDto
)

data class LeagueDto(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String?,
    val flag: String?,
    val season: Int?,
    val round: String?
)