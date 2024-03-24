package dev.jacobderynk.asterogram.data.repository

import dev.jacobderynk.asterogram.data.model.CommunicationResult
import dev.jacobderynk.asterogram.network.MeteoriteLandingsService
import dev.jacobderynk.asterogram.network.model.AsteroidResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRemoteRepositoryImpl(
    private val api: MeteoriteLandingsService
) : IAsteroidRemoteRepository {

    override suspend fun getAsteroids(
        limit: Int,
        offset: Int
    ): CommunicationResult<List<AsteroidResponse>> {
        return processResponse(withContext(Dispatchers.IO) {
            api.getAsteroids(
                limit = limit,
                offset = offset,
            )
        })
    }
}