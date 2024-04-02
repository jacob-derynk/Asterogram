package dev.jacobderynk.asterogram.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.jacobderynk.asterogram.data.model.LikedAsteroidEntity

@Dao
interface LikedAsteroidDao {
    @Insert
    suspend fun add(likedAsteroid: LikedAsteroidEntity)
    @Delete
    suspend fun delete(likedAsteroid: LikedAsteroidEntity)
    @Query("SELECT * FROM likedAsteroid WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: String): LikedAsteroidEntity?
}