package dev.jacobderynk.asterogram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param svg SVG tag as string
 */
@Entity(tableName = "asteroid")
data class AsteroidEntity(
    @PrimaryKey val id: String,
    val name: String,
    val svg: String? = null,
    val mass: String,
    val fall: String,
    val year: String,
    val `class`: String,
    val likes: Int,
    val isSaved: Boolean = false,
    val lat: String,
    val long: String,
)
