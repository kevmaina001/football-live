/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.domain.usecase

import com.score24seven.domain.model.Match
import com.score24seven.domain.repository.MatchRepository
import com.score24seven.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMatchDetailsUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    operator fun invoke(matchId: Int): Flow<Resource<Match>> {
        return repository.getMatchById(matchId)
    }

    suspend fun subscribeToLiveUpdates(matchId: Int) {
        repository.subscribeToLiveMatch(matchId)
    }

    suspend fun unsubscribeFromLiveUpdates(matchId: Int) {
        repository.unsubscribeFromLiveMatch(matchId)
    }
}