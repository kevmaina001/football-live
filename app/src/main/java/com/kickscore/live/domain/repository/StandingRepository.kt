/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.repository

import com.kickscore.live.domain.model.Standing
import com.kickscore.live.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface StandingRepository {

    fun getLeagueStandings(leagueId: Int, season: Int): Flow<Resource<List<Standing>>>

    suspend fun getLeagueStandingsSync(leagueId: Int, season: Int): Resource<List<Standing>>

    fun getGroupStandings(leagueId: Int, season: Int, group: String): Flow<Resource<List<Standing>>>

    suspend fun getTeamStanding(teamId: Int, season: Int): Resource<Standing?>

    suspend fun getTopTeams(leagueId: Int, season: Int, limit: Int = 5): Resource<List<Standing>>

    suspend fun getBottomTeams(leagueId: Int, season: Int, limit: Int = 5): Resource<List<Standing>>

    suspend fun refreshStandings(leagueId: Int, season: Int): Resource<List<Standing>>
}