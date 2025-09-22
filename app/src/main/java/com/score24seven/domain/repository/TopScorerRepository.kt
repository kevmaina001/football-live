/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.repository

import com.score24seven.domain.model.TopScorer
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TopScorerRepository {
    fun getTopScorers(leagueId: Int, season: Int): Flow<Resource<List<TopScorer>>>
}