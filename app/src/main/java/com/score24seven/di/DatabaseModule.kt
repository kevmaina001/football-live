/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.di

import android.content.Context
import androidx.room.Room
import com.score24seven.data.database.Score24SevenDatabase
import com.score24seven.data.database.dao.LeagueDao
import com.score24seven.data.database.dao.MatchDao
import com.score24seven.data.database.dao.RemoteKeyDao
import com.score24seven.data.database.dao.StandingDao
import com.score24seven.data.database.dao.TeamDao
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
    fun provideScore24SevenDatabase(@ApplicationContext context: Context): Score24SevenDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            Score24SevenDatabase::class.java,
            Score24SevenDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development only
            .build()
    }

    @Provides
    fun provideMatchDao(database: Score24SevenDatabase): MatchDao = database.matchDao()

    @Provides
    fun provideTeamDao(database: Score24SevenDatabase): TeamDao = database.teamDao()

    @Provides
    fun provideLeagueDao(database: Score24SevenDatabase): LeagueDao = database.leagueDao()

    @Provides
    fun provideStandingDao(database: Score24SevenDatabase): StandingDao = database.standingDao()

    @Provides
    fun provideRemoteKeyDao(database: Score24SevenDatabase): RemoteKeyDao = database.remoteKeyDao()
}