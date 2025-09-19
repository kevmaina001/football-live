/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.repository

import com.kickscore.live.data.api.EventDto
import com.kickscore.live.data.api.LineupDto
import com.kickscore.live.data.api.PredictionDto
import com.kickscore.live.data.api.StatisticsDto
import com.kickscore.live.data.dto.MatchDto
import com.kickscore.live.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MatchDetailRepository {
    fun getMatchPredictions(fixtureId: Int): Flow<Resource<List<PredictionDto>>>
    fun getMatchLineups(fixtureId: Int): Flow<Resource<List<LineupDto>>>
    fun getMatchStatistics(fixtureId: Int): Flow<Resource<List<StatisticsDto>>>
    fun getMatchEvents(fixtureId: Int): Flow<Resource<List<EventDto>>>
    fun getHeadToHead(homeTeamId: Int, awayTeamId: Int): Flow<Resource<List<MatchDto>>>
    fun getLeagueMatches(leagueId: Int, season: Int): Flow<Resource<List<MatchDto>>>
}