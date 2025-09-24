/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.repository

import com.score24seven.data.api.PredictionDto
import com.score24seven.data.dto.MatchDto
import com.score24seven.domain.model.*
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MatchDetailRepository {
    fun getMatchPredictions(fixtureId: Int): Flow<Resource<List<PredictionDto>>>
    fun getMatchLineups(fixtureId: Int): Flow<Resource<List<Lineup>>>
    fun getMatchStatistics(fixtureId: Int): Flow<Resource<List<MatchStatistic>>>
    fun getMatchEvents(fixtureId: Int): Flow<Resource<List<MatchEvent>>>
    fun getHeadToHead(homeTeamId: Int, awayTeamId: Int): Flow<Resource<List<Match>>>
    fun getLeagueMatches(leagueId: Int, season: Int): Flow<Resource<List<Match>>>
    fun getLeagueStandings(leagueId: Int, season: Int): Flow<Resource<List<Standing>>>
    fun getTeamFixtures(teamId: Int, season: Int, limit: Int = 5): Flow<Resource<List<Match>>>
}