package dev.jacobderynk.asterogram.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(asteroid: AsteroidEntity)

    @Insert
    suspend fun addAll(list: List<AsteroidEntity>)

    @Delete
    suspend fun delete(asteroid: AsteroidEntity)

    @Query("DELETE FROM asteroid WHERE isSaved = 0")
    suspend fun deleteUnsaved()

    @Query("UPDATE asteroid SET isSaved = :save, svg = :svg WHERE id = :id")
    suspend fun setIsSaved(id: String, save: Boolean, svg: String?)

    @Query("SELECT * FROM asteroid WHERE isSaved = 1")
    fun getSavedAsteroids(): Flow<List<AsteroidEntity>>

}