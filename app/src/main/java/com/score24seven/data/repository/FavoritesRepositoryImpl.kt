/*
 * Implementation of FavoritesRepository for managing favorite matches
 */

package com.score24seven.data.repository

import com.score24seven.data.database.Score24SevenDatabase
import com.score24seven.data.mapper.MatchEntityMapper
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.isLive
import com.score24seven.domain.repository.FavoritesRepository
import com.score24seven.util.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val database: Score24SevenDatabase,
    private val preferencesManager: PreferencesManager
) : FavoritesRepository {

    private val matchDao = database.matchDao()

    override fun getFavoriteMatches(): Flow<List<Match>> {
        // Use the reactive StateFlow from PreferencesManager
        return preferencesManager.favoriteMatchIds
            .mapLatest { favoriteIds ->
                favoriteIds.mapNotNull { matchId ->
                    try {
                        val entity = matchDao.getMatchById(matchId)
                        entity?.let { MatchEntityMapper.mapEntityToDomain(it) }
                    } catch (e: Exception) {
                        println("❌ ERROR: Failed to get match $matchId: ${e.message}")
                        null
                    }
                }.sortedWith(compareBy<Match> { !it.isLive() }.thenBy { it.fixture.timestamp })
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun addToFavorites(matchId: Int): Result<Unit> {
        return try {
            val currentFavorites = getFavoriteMatchIds().toMutableSet()
            currentFavorites.add(matchId)
            saveFavoriteMatchIds(currentFavorites.toList())
            println("💖 DEBUG: Added match $matchId to favorites")
            Result.success(Unit)
        } catch (e: Exception) {
            println("❌ ERROR: Failed to add match $matchId to favorites: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun removeFromFavorites(matchId: Int): Result<Unit> {
        return try {
            val currentFavorites = getFavoriteMatchIds().toMutableSet()
            currentFavorites.remove(matchId)
            saveFavoriteMatchIds(currentFavorites.toList())
            println("💔 DEBUG: Removed match $matchId from favorites")
            Result.success(Unit)
        } catch (e: Exception) {
            println("❌ ERROR: Failed to remove match $matchId from favorites: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun isFavorite(matchId: Int): Boolean {
        return getFavoriteMatchIds().contains(matchId)
    }

    override suspend fun getFavoriteMatchIds(): List<Int> {
        return preferencesManager.getFavoriteMatchIds()
    }

    override suspend fun clearAllFavorites(): Result<Unit> {
        return try {
            saveFavoriteMatchIds(emptyList())
            println("🗑️ DEBUG: Cleared all favorite matches")
            Result.success(Unit)
        } catch (e: Exception) {
            println("❌ ERROR: Failed to clear favorites: ${e.message}")
            Result.failure(e)
        }
    }

    private fun saveFavoriteMatchIds(favoriteIds: List<Int>) {
        preferencesManager.setFavoriteMatchIds(favoriteIds)
    }
}
