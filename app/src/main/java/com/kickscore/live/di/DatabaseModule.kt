/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.di

import android.content.Context
import androidx.room.Room
import com.kickscore.live.data.database.KickScoreDatabase
import com.kickscore.live.data.database.dao.LeagueDao
import com.kickscore.live.data.database.dao.MatchDao
import com.kickscore.live.data.database.dao.RemoteKeyDao
import com.kickscore.live.data.database.dao.StandingDao
import com.kickscore.live.data.database.dao.TeamDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideKickScoreDatabase(@ApplicationContext context: Context): KickScoreDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            KickScoreDatabase::class.java,
            KickScoreDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development only
            .build()
    }

    @Provides
    fun provideMatchDao(database: KickScoreDatabase): MatchDao = database.matchDao()

    @Provides
    fun provideTeamDao(database: KickScoreDatabase): TeamDao = database.teamDao()

    @Provides
    fun provideLeagueDao(database: KickScoreDatabase): LeagueDao = database.leagueDao()

    @Provides
    fun provideStandingDao(database: KickScoreDatabase): StandingDao = database.standingDao()

    @Provides
    fun provideRemoteKeyDao(database: KickScoreDatabase): RemoteKeyDao = database.remoteKeyDao()
}