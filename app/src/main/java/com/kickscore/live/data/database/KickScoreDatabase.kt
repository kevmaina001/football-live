/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.kickscore.live.data.database.converters.DateTimeConverters
import com.kickscore.live.data.database.converters.ListConverters
import com.kickscore.live.data.database.dao.LeagueDao
import com.kickscore.live.data.database.dao.MatchDao
import com.kickscore.live.data.database.dao.RemoteKeyDao
import com.kickscore.live.data.database.dao.StandingDao
import com.kickscore.live.data.database.dao.TeamDao
import com.kickscore.live.data.database.entity.FavoriteEntity
import com.kickscore.live.data.database.entity.LeagueEntity
import com.kickscore.live.data.database.entity.MatchEntity
import com.kickscore.live.data.database.entity.MatchEventEntity
import com.kickscore.live.data.database.entity.MatchLineupEntity
import com.kickscore.live.data.database.entity.MatchStatisticsEntity
import com.kickscore.live.data.database.entity.PlayerEntity
import com.kickscore.live.data.database.entity.RemoteKeyEntity
import com.kickscore.live.data.database.entity.StandingEntity
import com.kickscore.live.data.database.entity.TeamEntity

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
abstract class KickScoreDatabase : RoomDatabase() {

    abstract fun matchDao(): MatchDao
    abstract fun teamDao(): TeamDao
    abstract fun leagueDao(): LeagueDao
    abstract fun standingDao(): StandingDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "kickscore_database"

        @Volatile
        private var INSTANCE: KickScoreDatabase? = null

        fun getDatabase(context: Context): KickScoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KickScoreDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // For testing
        fun getInMemoryDatabase(context: Context): KickScoreDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                KickScoreDatabase::class.java
            ).build()
        }
    }
}