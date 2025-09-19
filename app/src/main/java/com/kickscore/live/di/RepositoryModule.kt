/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.di

import com.kickscore.live.data.repository.MatchRepositoryImpl
import com.kickscore.live.data.repository.MatchDetailRepositoryImpl
import com.kickscore.live.data.repository.LeagueInfoRepositoryImpl
import com.kickscore.live.domain.repository.MatchRepository
import com.kickscore.live.domain.repository.MatchDetailRepository
import com.kickscore.live.domain.repository.LeagueInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMatchRepository(
        matchRepositoryImpl: MatchRepositoryImpl
    ): MatchRepository

    @Binds
    @Singleton
    abstract fun bindMatchDetailRepository(
        matchDetailRepositoryImpl: MatchDetailRepositoryImpl
    ): MatchDetailRepository

    @Binds
    @Singleton
    abstract fun bindLeagueInfoRepository(
        leagueInfoRepositoryImpl: LeagueInfoRepositoryImpl
    ): LeagueInfoRepository
}