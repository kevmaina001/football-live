/*
 * League info repository interface
 */

package com.kickscore.live.domain.repository

import com.kickscore.live.domain.model.LeagueInfo
import com.kickscore.live.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface LeagueInfoRepository {
    fun getCurrentLeagues(): Flow<Resource<List<LeagueInfo>>>
    fun searchLeagues(query: String): Flow<Resource<List<LeagueInfo>>>
}