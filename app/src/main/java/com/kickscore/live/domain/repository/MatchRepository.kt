/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.repository

import androidx.paging.PagingData
import com.kickscore.live.domain.model.Match
import com.kickscore.live.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MatchRepository {

    fun getMatchesPaged(): Flow<PagingData<Match>>

    fun getLiveMatches(): Flow<Resource<List<Match>>>

    fun getLiveMatchesFlow(): Flow<List<Match>>

    fun getTodayMatches(): Flow<Resource<List<Match>>>

    fun getMatchById(matchId: Int): Flow<Resource<Match>>

    fun getTeamMatches(teamId: Int, season: Int): Flow<Resource<List<Match>>>

    fun getLeagueMatches(leagueId: Int, season: Int): Flow<Resource<List<Match>>>

    suspend fun subscribeToLiveMatch(matchId: Int)

    suspend fun unsubscribeFromLiveMatch(matchId: Int)

    fun searchMatches(query: String): Flow<Resource<List<Match>>>
}