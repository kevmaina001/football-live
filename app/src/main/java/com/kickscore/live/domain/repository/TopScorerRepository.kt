/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.repository

import com.kickscore.live.domain.model.TopScorer
import com.kickscore.live.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TopScorerRepository {
    fun getTopScorers(leagueId: Int, season: Int): Flow<Resource<List<TopScorer>>>
}