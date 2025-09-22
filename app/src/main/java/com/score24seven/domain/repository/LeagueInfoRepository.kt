/*
 * League info repository interface
 */

package com.score24seven.domain.repository

import com.score24seven.domain.model.LeagueInfo
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface LeagueInfoRepository {
    fun getCurrentLeagues(): Flow<Resource<List<LeagueInfo>>>
    fun searchLeagues(query: String): Flow<Resource<List<LeagueInfo>>>
}