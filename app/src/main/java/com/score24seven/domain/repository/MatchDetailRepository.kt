/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.repository

import com.score24seven.data.api.EventDto
import com.score24seven.data.api.LineupDto
import com.score24seven.data.api.PredictionDto
import com.score24seven.data.api.StatisticsDto
import com.score24seven.data.dto.MatchDto
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MatchDetailRepository {
    fun getMatchPredictions(fixtureId: Int): Flow<Resource<List<PredictionDto>>>
    fun getMatchLineups(fixtureId: Int): Flow<Resource<List<LineupDto>>>
    fun getMatchStatistics(fixtureId: Int): Flow<Resource<List<StatisticsDto>>>
    fun getMatchEvents(fixtureId: Int): Flow<Resource<List<EventDto>>>
    fun getHeadToHead(homeTeamId: Int, awayTeamId: Int): Flow<Resource<List<MatchDto>>>
    fun getLeagueMatches(leagueId: Int, season: Int): Flow<Resource<List<MatchDto>>>
}