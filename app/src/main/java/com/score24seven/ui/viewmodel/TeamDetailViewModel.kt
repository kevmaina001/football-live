/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.data.api.FootballApiService
import com.score24seven.data.mapper.toTeamStatistics
import com.score24seven.domain.model.*
import com.score24seven.domain.util.Resource
import com.score24seven.ui.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val apiService: FootballApiService
) : ViewModel() {

    private val _state = MutableStateFlow(TeamDetailState())
    val state: StateFlow<TeamDetailState> = _state.asStateFlow()

    private val _effects = Channel<TeamDetailEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var currentTeamId: Int? = null

    fun loadTeamDetails(teamId: Int) {
        println("🚀 DEBUG: TeamDetailViewModel.loadTeamDetails called with teamId: $teamId")
        currentTeamId = teamId
        updateState { copy(isRefreshing = true) }

        // FIXED: Use proper API loading first, then fallback if needed
        loadTeamBasicInfo(teamId)

        // Load fixtures for current season (2025)
        loadTeamFixturesSimple(teamId, 2025)

        // Load players using working squad endpoint approach
        loadTeamSquadsFast(teamId)

        // Skip statistics loading (not needed)
    }

    fun handleAction(action: TeamDetailAction) {
        when (action) {
            is TeamDetailAction.LoadTeamDetails -> {
                loadTeamDetails(action.teamId)
            }
            is TeamDetailAction.SelectTab -> {
                updateState { copy(selectedTab = action.tab) }
            }
            is TeamDetailAction.LoadTeamStatistics -> {
                loadTeamStatistics(action.teamId, action.leagueId, action.season)
            }
            is TeamDetailAction.LoadTeamFixtures -> {
                loadTeamFixturesSimple(action.teamId, 2025)
            }
            is TeamDetailAction.LoadTeamPlayers -> {
                loadTeamSquadsFast(action.teamId)
            }
            TeamDetailAction.ToggleFavorite -> {
                toggleFavorite()
            }
            TeamDetailAction.ShareTeam -> {
                shareTeam()
            }
            TeamDetailAction.RefreshAll -> {
                currentTeamId?.let { loadTeamDetails(it) }
            }
        }
    }

    private fun loadTeamBasicInfo(teamId: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(team = UiState.Loading) }
                println("🔍 DEBUG: Loading basic team info for teamId: $teamId")

                // Try to get team info from teams API with major leagues
                val leagues = listOf(39, 140, 135, 78, 61) // Premier League, La Liga, Serie A, Bundesliga, Ligue 1
                val seasons = listOf(2025, 2024)
                var teamLoaded = false

                for (leagueId in leagues) {
                    if (teamLoaded) break

                    for (season in seasons) {
                        if (!teamLoaded) {
                            try {
                                println("🔍 DEBUG: Trying teams API for league: $leagueId, season: $season")
                                val teamsResponse = apiService.getTeams(
                                    leagueId = leagueId,
                                    season = season
                                )

                                println("🔍 DEBUG: Teams API Response - Success: ${teamsResponse.isSuccessful}, Code: ${teamsResponse.code()}")

                                if (teamsResponse.isSuccessful) {
                                    val teams = teamsResponse.body()?.response
                                    val targetTeam = teams?.find { it.id == teamId }

                                    println("🔍 DEBUG: Found ${teams?.size ?: 0} teams, target team: $targetTeam")

                                    if (targetTeam != null) {
                                        val team = Team(
                                            id = targetTeam.id,
                                            name = targetTeam.name,
                                            code = targetTeam.code,
                                            logo = targetTeam.logo,
                                            country = targetTeam.country,
                                            founded = targetTeam.founded,
                                            isNational = targetTeam.national,
                                            isFavorite = false
                                        )

                                        println("🔍 DEBUG: Successfully loaded team via teams API: ${team.name}")
                                        updateState {
                                            copy(
                                                team = UiState.Success(team),
                                                isFavorite = team.isFavorite,
                                                isRefreshing = false
                                            )
                                        }
                                        teamLoaded = true
                                        break
                                    }
                                }
                            } catch (e: Exception) {
                                println("🔍 DEBUG: Exception in teams API call: ${e.message}")
                            }
                        }
                    }
                }

                if (!teamLoaded) {
                    println("🔍 DEBUG: Team not found in any league, using fallback")
                    loadTeamBasicInfoFallback(teamId)
                }
            } catch (e: Exception) {
                println("🔍 DEBUG: Exception loading team basic info: ${e.message}")
                loadTeamBasicInfoFallback(teamId)
            }
        }
    }

    private fun loadTeamBasicInfoFallback(teamId: Int) {
        viewModelScope.launch {
            try {
                println("🔍 DEBUG: Loading team basic info fallback for teamId: $teamId")

                // EXPANDED: Map more known team IDs to names and logos (real API team IDs)
                // Adding more teams to reduce "Team #unknown" cases for live matches
                val knownTeams = mapOf(
                    // Real API team IDs with logos
                    19 to Triple("Arsenal", "England", "https://media.api-sports.io/football/teams/19.png"),
                    8 to Triple("Liverpool", "England", "https://media.api-sports.io/football/teams/8.png"),
                    18 to Triple("Chelsea", "England", "https://media.api-sports.io/football/teams/18.png"),
                    50 to Triple("Manchester City", "England", "https://media.api-sports.io/football/teams/50.png"),
                    33 to Triple("Manchester United", "England", "https://media.api-sports.io/football/teams/33.png"),
                    40 to Triple("Tottenham", "England", "https://media.api-sports.io/football/teams/40.png"),
                    49 to Triple("Chelsea FC", "England", "https://media.api-sports.io/football/teams/49.png"),
                    42 to Triple("Arsenal FC", "England", "https://media.api-sports.io/football/teams/42.png"),
                    // More Premier League teams
                    35 to Triple("Bournemouth", "England", "https://media.api-sports.io/football/teams/35.png"),
                    36 to Triple("Fulham", "England", "https://media.api-sports.io/football/teams/36.png"),
                    45 to Triple("Everton", "England", "https://media.api-sports.io/football/teams/45.png"),
                    46 to Triple("Leicester City", "England", "https://media.api-sports.io/football/teams/46.png"),
                    47 to Triple("Newcastle United", "England", "https://media.api-sports.io/football/teams/47.png"),
                    // More Premier League teams
                    39 to Triple("Wolverhampton", "England", "https://media.api-sports.io/football/teams/39.png"),
                    38 to Triple("Watford", "England", "https://media.api-sports.io/football/teams/38.png"),
                    51 to Triple("Brighton", "England", "https://media.api-sports.io/football/teams/51.png"),
                    52 to Triple("Crystal Palace", "England", "https://media.api-sports.io/football/teams/52.png"),

                    // La Liga teams
                    541 to Triple("Real Madrid", "Spain", "https://media.api-sports.io/football/teams/541.png"),
                    529 to Triple("Barcelona", "Spain", "https://media.api-sports.io/football/teams/529.png"),
                    530 to Triple("Atletico Madrid", "Spain", "https://media.api-sports.io/football/teams/530.png"),
                    532 to Triple("Valencia", "Spain", "https://media.api-sports.io/football/teams/532.png"),
                    533 to Triple("Villarreal", "Spain", "https://media.api-sports.io/football/teams/533.png"),

                    // Serie A teams
                    489 to Triple("AC Milan", "Italy", "https://media.api-sports.io/football/teams/489.png"),
                    496 to Triple("Juventus", "Italy", "https://media.api-sports.io/football/teams/496.png"),
                    497 to Triple("AS Roma", "Italy", "https://media.api-sports.io/football/teams/497.png"),
                    487 to Triple("Lazio", "Italy", "https://media.api-sports.io/football/teams/487.png"),

                    // Bundesliga teams
                    157 to Triple("Bayern Munich", "Germany", "https://media.api-sports.io/football/teams/157.png"),
                    165 to Triple("Borussia Dortmund", "Germany", "https://media.api-sports.io/football/teams/165.png"),
                    173 to Triple("RB Leipzig", "Germany", "https://media.api-sports.io/football/teams/173.png"),

                    // Ligue 1 teams
                    85 to Triple("Paris Saint-Germain", "France", "https://media.api-sports.io/football/teams/85.png"),
                    80 to Triple("Lyon", "France", "https://media.api-sports.io/football/teams/80.png"),
                    81 to Triple("Marseille", "France", "https://media.api-sports.io/football/teams/81.png"),

                    // Mock ID mapping for testing
                    1 to Triple("Manchester City", "England", "https://media.api-sports.io/football/teams/50.png"),
                    2 to Triple("Arsenal", "England", "https://media.api-sports.io/football/teams/19.png"),
                    3 to Triple("Liverpool", "England", "https://media.api-sports.io/football/teams/8.png"),
                    4 to Triple("Chelsea", "England", "https://media.api-sports.io/football/teams/18.png")
                )

                // ENHANCED FALLBACK: Try to use better default or make API call
                var teamInfo = knownTeams[teamId]

                if (teamInfo == null) {
                    println("🔴 DEBUG: UNKNOWN TEAM DETECTED! TeamId: $teamId not found in knownTeams mapping")
                    println("🔴 DEBUG: Available team IDs in mapping: ${knownTeams.keys.sorted()}")

                    // Try to get team info from a simple teams API call as last resort
                    try {
                        println("🔍 DEBUG: Attempting direct teams API call for team $teamId")
                        val directResponse = apiService.getTeams(leagueId = 39, season = 2025) // Try Premier League
                        val teams = directResponse.body()?.response
                        val foundTeam = teams?.find { it.id == teamId }

                        if (foundTeam != null) {
                            println("🔍 DEBUG: Found team via direct API: ${foundTeam.name}")
                            teamInfo = Triple(foundTeam.name, foundTeam.country ?: "Unknown", foundTeam.logo ?: "https://media.api-sports.io/football/teams/$teamId.png")
                        } else {
                            println("🔴 DEBUG: Team $teamId not found in Premier League, using fallback")
                            teamInfo = Triple("Team #$teamId", "Unknown", "https://media.api-sports.io/football/teams/$teamId.png")
                        }
                    } catch (e: Exception) {
                        println("🔴 DEBUG: Direct API call failed: ${e.message}")
                        teamInfo = Triple("Team #$teamId", "Unknown", "https://media.api-sports.io/football/teams/$teamId.png")
                    }
                }

                val team = Team(
                    id = teamId,
                    name = teamInfo?.first ?: "Team #$teamId",
                    code = null,
                    logo = teamInfo?.third,
                    country = teamInfo?.second ?: "Unknown",
                    founded = null,
                    isNational = false,
                    isFavorite = false
                )

                println("🔍 DEBUG: Created fallback team: ${team.name}")

                updateState {
                    copy(
                        team = UiState.Success(team),
                        isFavorite = team.isFavorite,
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                println("🔍 DEBUG: Exception in fallback: ${e.message}")
                updateState {
                    copy(
                        team = UiState.Error(e.message ?: "Failed to load team details"),
                        isRefreshing = false
                    )
                }
            }
        }
    }

    private fun tryLoadStatisticsFromMultipleLeagues(teamId: Int) {
        viewModelScope.launch {
            updateState { copy(statistics = UiState.Loading) }
            println("🔍 DEBUG: Starting statistics loading for teamId: $teamId")

            // Try major leagues for faster loading
            val leagues = listOf(
                39,  // Premier League
                140, // La Liga
                135, // Serie A
                78,  // Bundesliga
                61   // Ligue 1
            )

            // Try current season first, then previous seasons
            val seasons = listOf(2024, 2023) // Focus on seasons with data
            var statisticsLoaded = false

            for (season in seasons) {
                if (statisticsLoaded) break
                println("🔍 DEBUG: Trying season: $season")

                for (leagueId in leagues) {
                    if (!statisticsLoaded) {
                        try {
                            println("🔍 DEBUG: Trying league: $leagueId, season: $season, team: $teamId")
                            val response = apiService.getTeamStatistics(
                                teamId = teamId,
                                leagueId = leagueId,
                                season = season
                            )

                            println("🔍 DEBUG: API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                            if (response.isSuccessful) {
                                val statisticsDto = response.body()?.response
                                println("🔍 DEBUG: Statistics DTO: $statisticsDto")

                                if (statisticsDto != null) {
                                    val statistics = statisticsDto.toTeamStatistics()
                                    println("🔍 DEBUG: Statistics loaded successfully for league: $leagueId, season: $season")
                                    updateState { copy(statistics = UiState.Success(statistics)) }
                                    statisticsLoaded = true
                                    break
                                } else {
                                    println("🔍 DEBUG: Statistics DTO was null")
                                }
                            } else {
                                println("🔍 DEBUG: API failed - ${response.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            println("🔍 DEBUG: Exception loading statistics: ${e.message}")
                        }
                    }
                }
            }

            if (!statisticsLoaded) {
                println("🔍 DEBUG: No statistics loaded for any league/season")
                updateState { copy(statistics = UiState.Error("No statistics available for this team")) }
            }
        }
    }

    private fun loadTeamStatistics(teamId: Int, leagueId: Int, season: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(statistics = UiState.Loading) }

                val response = apiService.getTeamStatistics(
                    teamId = teamId,
                    leagueId = leagueId,
                    season = season
                )

                if (response.isSuccessful) {
                    val statisticsDto = response.body()?.response
                    if (statisticsDto != null) {
                        val statistics = statisticsDto.toTeamStatistics()
                        updateState { copy(statistics = UiState.Success(statistics)) }
                    } else {
                        updateState { copy(statistics = UiState.Error("No statistics available")) }
                    }
                } else {
                    updateState { copy(statistics = UiState.Error("Failed to load statistics")) }
                }
            } catch (e: Exception) {
                updateState { copy(statistics = UiState.Error(e.message ?: "Unknown error")) }
            }
        }
    }

    private fun loadTeamFixturesSimple(teamId: Int, season: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(fixtures = UiState.Loading) }
                println("🔍 DEBUG: Loading fixtures for team: $teamId, season: $season")

                val response = apiService.getTeamFixtures(
                    teamId = teamId,
                    season = season
                )

                println("🔍 DEBUG: Fixtures API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                if (response.isSuccessful) {
                    val fixtures = response.body()?.response?.mapNotNull { matchDto ->
                        try {
                            Match(
                                id = matchDto.fixture.id,
                                homeTeam = Team(
                                    id = matchDto.teams.home.id,
                                    name = matchDto.teams.home.name,
                                    logo = matchDto.teams.home.logo
                                ),
                                awayTeam = Team(
                                    id = matchDto.teams.away.id,
                                    name = matchDto.teams.away.name,
                                    logo = matchDto.teams.away.logo
                                ),
                                league = League(
                                    id = matchDto.league.id,
                                    name = matchDto.league.name,
                                    country = matchDto.league.country,
                                    logo = matchDto.league.logo,
                                    flag = matchDto.league.flag,
                                    season = matchDto.league.season,
                                    round = matchDto.league.round
                                ),
                                fixture = Fixture(
                                    dateTime = java.time.LocalDateTime.now(), // Skip complex parsing for speed
                                    timezone = matchDto.fixture.timezone ?: "UTC",
                                    timestamp = matchDto.fixture.timestamp
                                ),
                                score = Score(
                                    home = matchDto.goals.home,
                                    away = matchDto.goals.away,
                                    halftime = matchDto.score.halftime?.let {
                                        ScorePeriod(it.home, it.away)
                                    },
                                    fulltime = matchDto.score.fulltime?.let {
                                        ScorePeriod(it.home, it.away)
                                    }
                                ),
                                status = MatchStatus(
                                    short = matchDto.fixture.status.short,
                                    long = matchDto.fixture.status.long,
                                    elapsed = matchDto.fixture.status.elapsed
                                )
                            )
                        } catch (e: Exception) {
                            println("🔍 DEBUG: Error parsing match: ${e.message}")
                            null
                        }
                    } ?: emptyList()

                    // Sort fixtures by date and separate played vs upcoming
                    val sortedFixtures = fixtures.sortedBy { it.fixture.timestamp }
                    val now = System.currentTimeMillis() / 1000 // Current timestamp in seconds

                    println("🔍 DEBUG: Loaded ${fixtures.size} fixtures, sorting by date")

                    updateState { copy(fixtures = UiState.Success(sortedFixtures)) }
                } else {
                    println("🔍 DEBUG: Fixtures API failed with code ${response.code()}")
                    updateState { copy(fixtures = UiState.Error("Failed to load fixtures")) }
                }
            } catch (e: Exception) {
                println("🔍 DEBUG: Exception loading fixtures: ${e.message}")
                updateState { copy(fixtures = UiState.Error("Unable to load fixtures")) }
            }
        }
    }

    private fun loadTeamSquadsFast(teamId: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(players = UiState.Loading) }
                println("🔍 DEBUG: Loading squad fast for team: $teamId")

                // Use the working squad endpoint like your friend's implementation
                val response = apiService.getTeamSquads(teamId = teamId)

                println("🔍 DEBUG: Squad API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                if (response.isSuccessful) {
                    val squads = response.body()?.response
                    if (!squads.isNullOrEmpty()) {
                        // Convert squad players to Player domain model
                        val players = squads.first().players.map { squadPlayer ->
                            Player(
                                id = squadPlayer.id,
                                name = squadPlayer.name,
                                firstName = null,
                                lastName = null,
                                age = squadPlayer.age,
                                nationality = null,
                                position = squadPlayer.position,
                                number = squadPlayer.number,
                                photo = squadPlayer.photo,
                                isInjured = false
                            )
                        }

                        println("🔍 DEBUG: Loaded ${players.size} squad players")
                        updateState { copy(players = UiState.Success(players)) }
                    } else {
                        println("🔍 DEBUG: No squad data found")
                        updateState { copy(players = UiState.Error("No squad data available")) }
                    }
                } else {
                    println("🔍 DEBUG: Squad API failed with code ${response.code()}, trying fallback")
                    // Fallback to regular players API
                    loadTeamPlayersFallbackOnly(teamId)
                }
            } catch (e: Exception) {
                println("🔍 DEBUG: Exception loading squad: ${e.message}")
                // Fallback to regular players API
                loadTeamPlayersFallbackOnly(teamId)
            }
        }
    }

    private fun loadTeamPlayersFallbackOnly(teamId: Int) {
        viewModelScope.launch {
            try {
                println("🔍 DEBUG: Trying fallback players API for team: $teamId")
                val response = apiService.getPlayers(
                    teamId = teamId,
                    season = 2024, // Use 2024 season as it's more likely to have data
                    page = 1
                )

                println("🔍 DEBUG: Fallback Players API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                if (response.isSuccessful) {
                    val players = response.body()?.response?.mapNotNull { playerDto ->
                        try {
                            Player(
                                id = playerDto.id,
                                name = playerDto.name,
                                firstName = playerDto.firstname,
                                lastName = playerDto.lastname,
                                age = playerDto.age,
                                nationality = playerDto.nationality,
                                position = playerDto.statistics?.firstOrNull()?.games?.position,
                                number = playerDto.statistics?.firstOrNull()?.games?.number,
                                photo = playerDto.photo,
                                isInjured = playerDto.injured ?: false
                            )
                        } catch (e: Exception) {
                            println("🔍 DEBUG: Error parsing player: ${e.message}")
                            null
                        }
                    } ?: emptyList()

                    if (players.isNotEmpty()) {
                        println("🔍 DEBUG: Loaded ${players.size} fallback players")
                        updateState { copy(players = UiState.Success(players)) }
                    } else {
                        updateState { copy(players = UiState.Error("No players available for this team")) }
                    }
                } else {
                    updateState { copy(players = UiState.Error("Failed to load players")) }
                }
            } catch (e: Exception) {
                println("🔍 DEBUG: Exception in fallback players: ${e.message}")
                updateState { copy(players = UiState.Error("Unable to load players")) }
            }
        }
    }

    private fun loadTeamPlayersSimpleOld(teamId: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(players = UiState.Loading) }
                println("🔍 DEBUG: Loading players simple for team: $teamId")

                // Single fast API call - current season only
                val response = apiService.getPlayers(
                    teamId = teamId,
                    season = 2025, // Current season
                    page = 1
                )

                println("🔍 DEBUG: Players API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                if (response.isSuccessful) {
                    val players = response.body()?.response?.mapNotNull { playerDto ->
                        try {
                            Player(
                                id = playerDto.id,
                                name = playerDto.name,
                                firstName = playerDto.firstname,
                                lastName = playerDto.lastname,
                                age = playerDto.age,
                                nationality = playerDto.nationality,
                                position = playerDto.statistics?.firstOrNull()?.games?.position,
                                number = playerDto.statistics?.firstOrNull()?.games?.number,
                                photo = playerDto.photo,
                                isInjured = playerDto.injured ?: false
                            )
                        } catch (e: Exception) {
                            println("🔍 DEBUG: Error parsing player: ${e.message}")
                            null
                        }
                    } ?: emptyList()

                    if (players.isNotEmpty()) {
                        println("🔍 DEBUG: Loaded ${players.size} players")
                        updateState { copy(players = UiState.Success(players)) }
                    } else {
                        println("🔍 DEBUG: No players found, trying fallback")
                        // Try 2024 season as fallback
                        loadTeamPlayersFallback(teamId)
                    }
                } else {
                    println("🔍 DEBUG: Players API failed, trying fallback")
                    loadTeamPlayersFallback(teamId)
                }
            } catch (e: Exception) {
                println("🔍 DEBUG: Exception loading players: ${e.message}")
                updateState { copy(players = UiState.Error("Unable to load players")) }
            }
        }
    }

    private fun loadTeamPlayersFallback(teamId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getPlayers(
                    teamId = teamId,
                    season = 2024, // Previous season fallback
                    page = 1
                )

                if (response.isSuccessful) {
                    val players = response.body()?.response?.mapNotNull { playerDto ->
                        try {
                            Player(
                                id = playerDto.id,
                                name = playerDto.name,
                                firstName = playerDto.firstname,
                                lastName = playerDto.lastname,
                                age = playerDto.age,
                                nationality = playerDto.nationality,
                                position = playerDto.statistics?.firstOrNull()?.games?.position,
                                number = playerDto.statistics?.firstOrNull()?.games?.number,
                                photo = playerDto.photo,
                                isInjured = playerDto.injured ?: false
                            )
                        } catch (e: Exception) {
                            null
                        }
                    } ?: emptyList()

                    if (players.isNotEmpty()) {
                        updateState { copy(players = UiState.Success(players)) }
                    } else {
                        updateState { copy(players = UiState.Error("No players available")) }
                    }
                } else {
                    updateState { copy(players = UiState.Error("Failed to load players")) }
                }
            } catch (e: Exception) {
                updateState { copy(players = UiState.Error("Unable to load players")) }
            }
        }
    }

    private fun loadTeamSquadsOld(teamId: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(players = UiState.Loading) }
                println("🔍 DEBUG: Loading squad for team: $teamId")

                val response = apiService.getTeamSquads(teamId = teamId)

                println("🔍 DEBUG: Squad API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                if (response.isSuccessful) {
                    val squads = response.body()?.response
                    if (!squads.isNullOrEmpty()) {
                        val players = squads.first().players.map { squadPlayer ->
                            Player(
                                id = squadPlayer.id,
                                name = squadPlayer.name,
                                firstName = null,
                                lastName = null,
                                age = squadPlayer.age,
                                nationality = null,
                                position = squadPlayer.position,
                                number = squadPlayer.number,
                                photo = squadPlayer.photo,
                                isInjured = false
                            )
                        }

                        println("🔍 DEBUG: Loaded ${players.size} squad players")
                        updateState { copy(players = UiState.Success(players)) }
                    } else {
                        println("🔍 DEBUG: No squad data found")
                        updateState { copy(players = UiState.Error("No squad data available")) }
                    }
                } else {
                    println("🔍 DEBUG: Squad API failed with code ${response.code()}")
                    updateState { copy(players = UiState.Error("Failed to load squad")) }
                }
            } catch (e: Exception) {
                println("🔍 DEBUG: Exception loading squad: ${e.message}")
                updateState { copy(players = UiState.Error("Unable to load squad")) }
            }
        }
    }

    private fun loadTeamFixturesOld(teamId: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(fixtures = UiState.Loading) }
                println("🔍 DEBUG: Starting fixtures loading for teamId: $teamId")

                // Try current season first, then previous seasons
                val seasons = listOf(2025, 2024, 2023)
                var fixturesLoaded = false

                for (season in seasons) {
                    if (!fixturesLoaded) {
                        try {
                            println("🔍 DEBUG: Trying fixtures for season: $season, team: $teamId")
                            val response = apiService.getTeamFixtures(
                                teamId = teamId,
                                season = season,
                                last = 10
                            )
                            println("🔍 DEBUG: Fixtures API Response - Success: ${response.isSuccessful}, Code: ${response.code()}")

                            if (response.isSuccessful) {
                                val fixtures = response.body()?.response?.mapNotNull { matchDto ->
                                    try {
                                        // Map MatchDto to Match domain model with error handling
                                        Match(
                                            id = matchDto.fixture.id,
                                            homeTeam = Team(
                                                id = matchDto.teams.home.id,
                                                name = matchDto.teams.home.name,
                                                logo = matchDto.teams.home.logo
                                            ),
                                            awayTeam = Team(
                                                id = matchDto.teams.away.id,
                                                name = matchDto.teams.away.name,
                                                logo = matchDto.teams.away.logo
                                            ),
                                            league = League(
                                                id = matchDto.league.id,
                                                name = matchDto.league.name,
                                                country = matchDto.league.country,
                                                logo = matchDto.league.logo,
                                                flag = matchDto.league.flag,
                                                season = matchDto.league.season,
                                                round = matchDto.league.round
                                            ),
                                            fixture = Fixture(
                                                dateTime = java.time.LocalDateTime.now(), // TODO: Parse actual date
                                                timezone = matchDto.fixture.timezone,
                                                timestamp = matchDto.fixture.timestamp
                                            ),
                                            score = Score(
                                                home = matchDto.goals.home,
                                                away = matchDto.goals.away,
                                                halftime = matchDto.score.halftime?.let {
                                                    ScorePeriod(it.home, it.away)
                                                },
                                                fulltime = matchDto.score.fulltime?.let {
                                                    ScorePeriod(it.home, it.away)
                                                }
                                            ),
                                            status = MatchStatus(
                                                short = matchDto.fixture.status.short,
                                                long = matchDto.fixture.status.long,
                                                elapsed = matchDto.fixture.status.elapsed
                                            )
                                        )
                                    } catch (e: Exception) {
                                        null // Skip this match if parsing fails
                                    }
                                } ?: emptyList()

                                if (fixtures.isNotEmpty()) {
                                    updateState { copy(fixtures = UiState.Success(fixtures)) }
                                    fixturesLoaded = true
                                }
                            }
                        } catch (e: Exception) {
                            // Continue trying other seasons
                        }
                    }
                }

                if (!fixturesLoaded) {
                    updateState { copy(fixtures = UiState.Error("No fixtures available for this team")) }
                }
            } catch (e: Exception) {
                updateState { copy(fixtures = UiState.Error("Unable to load fixtures")) }
            }
        }
    }

    private fun loadTeamPlayers(teamId: Int) {
        viewModelScope.launch {
            try {
                updateState { copy(players = UiState.Loading) }
                println("🔍 DEBUG: Starting players loading for teamId: $teamId")

                // Try current season first, then previous seasons
                val seasons = listOf(2025, 2024, 2023)
                var playersLoaded = false

                for (season in seasons) {
                    if (!playersLoaded) {
                        try {
                            println("🔍 DEBUG: Trying players for season: $season, team: $teamId")

                            // Try with league parameter first (more likely to succeed)
                            val leagues = listOf(39, 140, 135, 78, 61) // Major leagues
                            var playersResponse: retrofit2.Response<com.score24seven.data.dto.ApiResponse<List<com.score24seven.data.api.PlayerDto>>>? = null

                            for (leagueId in leagues) {
                                try {
                                    println("🔍 DEBUG: Trying players with league: $leagueId, season: $season, team: $teamId")
                                    playersResponse = apiService.getPlayers(
                                        teamId = teamId,
                                        leagueId = leagueId,
                                        season = season,
                                        page = 1
                                    )
                                    if (playersResponse.isSuccessful && !playersResponse.body()?.response.isNullOrEmpty()) {
                                        println("🔍 DEBUG: Found players with league: $leagueId")
                                        break
                                    }
                                } catch (e: Exception) {
                                    println("🔍 DEBUG: Failed with league $leagueId: ${e.message}")
                                }
                            }

                            // Fallback: try without league parameter
                            if (playersResponse?.isSuccessful != true || playersResponse.body()?.response.isNullOrEmpty()) {
                                println("🔍 DEBUG: Trying players without league parameter")
                                playersResponse = apiService.getPlayers(
                                    teamId = teamId,
                                    season = season,
                                    page = 1
                                )
                            }

                            println("🔍 DEBUG: Final players API Response - Success: ${playersResponse.isSuccessful}, Code: ${playersResponse.code()}")

                            if (playersResponse.isSuccessful) {
                                val players = playersResponse.body()?.response?.mapNotNull { playerDto ->
                                    try {
                                        Player(
                                            id = playerDto.id,
                                            name = playerDto.name,
                                            firstName = playerDto.firstname,
                                            lastName = playerDto.lastname,
                                            age = playerDto.age,
                                            nationality = playerDto.nationality,
                                            position = playerDto.statistics?.firstOrNull()?.games?.position,
                                            number = playerDto.statistics?.firstOrNull()?.games?.number,
                                            photo = playerDto.photo,
                                            isInjured = playerDto.injured ?: false
                                        )
                                    } catch (e: Exception) {
                                        null // Skip this player if parsing fails
                                    }
                                } ?: emptyList()

                                if (players.isNotEmpty()) {
                                    updateState { copy(players = UiState.Success(players)) }
                                    playersLoaded = true
                                }
                            }
                        } catch (e: Exception) {
                            // Continue trying other seasons
                        }
                    }
                }

                if (!playersLoaded) {
                    updateState { copy(players = UiState.Error("No players available for this team")) }
                }
            } catch (e: Exception) {
                updateState { copy(players = UiState.Error("Unable to load players")) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val currentFavorite = _state.value.isFavorite
                updateState { copy(isFavorite = !currentFavorite) }

                // TODO: Update favorite status in local database

                val message = if (!currentFavorite) "Added to favorites" else "Removed from favorites"
                _effects.send(TeamDetailEffect.ShowSnackbar(message))
            } catch (e: Exception) {
                _effects.send(TeamDetailEffect.ShowError("Failed to update favorite status"))
            }
        }
    }

    fun shareTeam() {
        viewModelScope.launch {
            try {
                val team = _state.value.team
                if (team is UiState.Success) {
                    val shareText = "Check out ${team.data.name} team details!"
                    _effects.send(TeamDetailEffect.ShareTeam(shareText))
                } else {
                    _effects.send(TeamDetailEffect.ShowError("No team data to share"))
                }
            } catch (e: Exception) {
                _effects.send(TeamDetailEffect.ShowError("Failed to share team"))
            }
        }
    }

    private fun updateState(update: TeamDetailState.() -> TeamDetailState) {
        _state.value = _state.value.update()
    }
}