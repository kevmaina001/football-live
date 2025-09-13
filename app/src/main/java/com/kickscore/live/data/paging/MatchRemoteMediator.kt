/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kickscore.live.data.api.FootballApiService
import com.kickscore.live.data.database.KickScoreDatabase
import com.kickscore.live.data.database.entity.MatchEntity
import com.kickscore.live.data.database.entity.RemoteKeyEntity
import com.kickscore.live.data.mapper.MatchMapper
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagingApi::class)
class MatchRemoteMediator(
    private val apiService: FootballApiService,
    private val database: KickScoreDatabase
) : RemoteMediator<Int, MatchEntity>() {

    private val matchDao = database.matchDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = 60 * 60 * 1000L // 1 hour
        val lastRefresh = remoteKeyDao.getLatestRemoteKey()?.lastRefresh ?: 0

        return if (System.currentTimeMillis() - lastRefresh < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MatchEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKey?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKey?.prevKey
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKey?.nextKey
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    nextKey
                }
            }

            // Get current date for today's matches
            val today = LocalDateTime.now()
            val dateString = today.format(DateTimeFormatter.ISO_LOCAL_DATE)

            val response = apiService.getTodayFixtures(dateString)

            if (!response.isSuccessful) {
                return MediatorResult.Error(
                    HttpException(response)
                )
            }

            val apiResponse = response.body()
            val matches = apiResponse?.response ?: emptyList()

            val endOfPaginationReached = matches.isEmpty() || matches.size < state.config.pageSize

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = matches.map {
                    RemoteKeyEntity(
                        id = it.fixtureId.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeyDao.insertRemoteKeys(keys)
                matchDao.insertMatches(MatchMapper.mapDtosToEntities(matches))
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MatchEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { match ->
            remoteKeyDao.getRemoteKey(match.id.toString())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MatchEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { match ->
            remoteKeyDao.getRemoteKey(match.id.toString())
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MatchEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { matchId ->
                remoteKeyDao.getRemoteKey(matchId.toString())
            }
        }
    }
}