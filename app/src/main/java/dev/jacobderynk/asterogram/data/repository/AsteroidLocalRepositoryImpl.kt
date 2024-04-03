package dev.jacobderynk.asterogram.data.repository

import dev.jacobderynk.asterogram.data.database.AsteroidDao
import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import dev.jacobderynk.asterogram.data.network.response.AsteroidResponse
import kotlinx.coroutines.flow.Flow

class AsteroidLocalRepositoryImpl(
    private val asteroidDao: AsteroidDao,
) : IAsteroidLocalRepository {
    override suspend fun insertAsteroid(asteroid: AsteroidEntity) {
        asteroidDao.add(asteroid)
    }

    override suspend fun insertAllAsteroids(asteroids: List<AsteroidEntity>) {
        asteroidDao.addAll(asteroids)
    }

    override suspend fun delete(asteroid: AsteroidEntity) {
        asteroidDao.delete(asteroid)
    }

    override suspend fun deleteUnsavedAsteroids() {
        asteroidDao.deleteUnsaved()
    }

    override fun getSavedAsteroids(): Flow<List<AsteroidEntity>> {
        return asteroidDao.getSavedAsteroids()
    }

}

fun AsteroidResponse.toDbModel() =
    AsteroidEntity(
        id = this.id,
        name = this.name,
        svg = null,
        mass = this.mass,
        fall = this.fall,
        year = this.year,
        likes = 0,
        isSaved = false,
        lat = this.recLat,
        long = this.recLong
    )
