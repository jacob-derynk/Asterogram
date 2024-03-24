package dev.jacobderynk.asterogram.data.repository

import dev.jacobderynk.asterogram.data.model.CommunicationResult
import dev.jacobderynk.asterogram.network.model.AsteroidResponse

interface IAsteroidRemoteRepository : IBaseRepository {

    suspend fun getAsteroids(
        limit: Int,
        offset: Int
    ): CommunicationResult<List<AsteroidResponse>>
}