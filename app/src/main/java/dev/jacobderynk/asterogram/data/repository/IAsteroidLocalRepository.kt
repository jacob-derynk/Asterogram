package dev.jacobderynk.asterogram.data.repository

import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import dev.jacobderynk.asterogram.data.network.response.AsteroidResponse
import kotlinx.coroutines.flow.Flow

interface IAsteroidLocalRepository {
    suspend fun insertAsteroid(asteroid: AsteroidEntity)
    suspend fun insertAllAsteroids(asteroids: List<AsteroidEntity>)
    suspend fun delete(asteroid: AsteroidEntity)
    suspend fun deleteUnsavedAsteroids()
    fun getSavedAsteroids(): Flow<List<AsteroidEntity>>
}