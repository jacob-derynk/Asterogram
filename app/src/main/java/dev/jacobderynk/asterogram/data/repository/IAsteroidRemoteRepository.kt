package dev.jacobderynk.asterogram.data.repository

import dev.jacobderynk.asterogram.data.model.CommunicationResult
import dev.jacobderynk.asterogram.data.network.response.AsteroidResponse

interface IAsteroidRemoteRepository : IBaseRepository {

    suspend fun getAsteroids(
        limit: Int,
        offset: Int
    ): CommunicationResult<List<AsteroidResponse>>
}