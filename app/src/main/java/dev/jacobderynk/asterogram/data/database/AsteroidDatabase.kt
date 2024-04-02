package dev.jacobderynk.asterogram.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.jacobderynk.asterogram.BuildConfig
import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import dev.jacobderynk.asterogram.data.model.LikedAsteroidEntity

@Database(
    entities = [AsteroidEntity::class, LikedAsteroidEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDao() : AsteroidDao
    abstract fun likedAsteroidDao() : LikedAsteroidDao

    companion object {
        private var INSTANCE: AsteroidDatabase? = null

        fun getDatabase(context: Context): AsteroidDatabase {
            if (INSTANCE == null) {
                synchronized(AsteroidDatabase::class.java) {
                    if (INSTANCE == null) {
                        if (BuildConfig.DEBUG) {
                            INSTANCE = Room.databaseBuilder(
                                context.applicationContext,
                                AsteroidDatabase::class.java,
                                "asteroid_database"
                            )
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build()
                        } else {
                            INSTANCE = Room.databaseBuilder(
                                context.applicationContext,
                                AsteroidDatabase::class.java,
                                "asteroid_database"
                            )
                                .build()
                        }
                    }
                }
            }
            return INSTANCE!!
        }
    }
}