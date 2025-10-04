/*
 * Repository interface for managing favorite matches
 */

package com.score24seven.domain.repository

import com.score24seven.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    /**
     * Get all favorite matches
     */
    fun getFavoriteMatches(): Flow<List<Match>>

    /**
     * Add a match to favorites
     */
    suspend fun addToFavorites(matchId: Int): Result<Unit>

    /**
     * Remove a match from favorites
     */
    suspend fun removeFromFavorites(matchId: Int): Result<Unit>

    /**
     * Check if a match is in favorites
     */
    suspend fun isFavorite(matchId: Int): Boolean

    /**
     * Get favorite match IDs
     */
    suspend fun getFavoriteMatchIds(): List<Int>

    /**
     * Clear all favorites
     */
    suspend fun clearAllFavorites(): Result<Unit>
}