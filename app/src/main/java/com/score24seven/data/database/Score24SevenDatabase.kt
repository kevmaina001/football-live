/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.score24seven.data.database.converters.DateTimeConverters
import com.score24seven.data.database.converters.ListConverters
import com.score24seven.data.database.dao.LeagueDao
import com.score24seven.data.database.dao.MatchDao
import com.score24seven.data.database.dao.RemoteKeyDao
import com.score24seven.data.database.dao.StandingDao
import com.score24seven.data.database.dao.TeamDao
import com.score24seven.data.database.entity.FavoriteEntity
import com.score24seven.data.database.entity.LeagueEntity
import com.score24seven.data.database.entity.MatchEntity
import com.score24seven.data.database.entity.MatchEventEntity
import com.score24seven.data.database.entity.MatchLineupEntity
import com.score24seven.data.database.entity.MatchStatisticsEntity
import com.score24seven.data.database.entity.PlayerEntity
import com.score24seven.data.database.entity.RemoteKeyEntity
import com.score24seven.data.database.entity.StandingEntity
import com.score24seven.data.database.entity.TeamEntity

@Database(
    entities = [
        MatchEntity::class,
        TeamEntity::class,
        LeagueEntity::class,
        PlayerEntity::class,
        MatchEventEntity::class,
        MatchLineupEntity::class,
        MatchStatisticsEntity::class,
        StandingEntity::class,
        FavoriteEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateTimeConverters::class,
    ListConverters::class
)
abstract class Score24SevenDatabase : RoomDatabase() {

    abstract fun matchDao(): MatchDao
    abstract fun teamDao(): TeamDao
    abstract fun leagueDao(): LeagueDao
    abstract fun standingDao(): StandingDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "score24seven_database"

        @Volatile
        private var INSTANCE: Score24SevenDatabase? = null

        fun getDatabase(context: Context): Score24SevenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Score24SevenDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // For testing
        fun getInMemoryDatabase(context: Context): Score24SevenDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                Score24SevenDatabase::class.java
            ).build()
        }
    }
}