package dev.jacobderynk.asterogram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * When user likes a post, its saved as [LikedAsteroidEntity] into local DB
 */
@Entity(tableName = "likedAsteroid")
data class LikedAsteroidEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val remoteId: String,
)