/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.di

import com.score24seven.data.repository.MatchRepositoryImpl
import com.score24seven.data.repository.MatchDetailRepositoryImpl
import com.score24seven.data.repository.LeagueInfoRepositoryImpl
import com.score24seven.data.repository.StandingRepositoryImpl
import com.score24seven.data.repository.TeamRepositoryImpl
import com.score24seven.data.repository.TopScorerRepositoryImpl
import com.score24seven.domain.repository.MatchRepository
import com.score24seven.domain.repository.MatchDetailRepository
import com.score24seven.domain.repository.LeagueInfoRepository
import com.score24seven.domain.repository.StandingRepository
import com.score24seven.domain.repository.TeamRepository
import com.score24seven.domain.repository.TopScorerRepository
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

    @Binds
    @Singleton
    abstract fun bindStandingRepository(
        standingRepositoryImpl: StandingRepositoryImpl
    ): StandingRepository

    @Binds
    @Singleton
    abstract fun bindTeamRepository(
        teamRepositoryImpl: TeamRepositoryImpl
    ): TeamRepository

    @Binds
    @Singleton
    abstract fun bindTopScorerRepository(
        topScorerRepositoryImpl: TopScorerRepositoryImpl
    ): TopScorerRepository
}