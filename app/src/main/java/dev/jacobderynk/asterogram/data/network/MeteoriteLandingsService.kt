package dev.jacobderynk.asterogram.data.network

import dev.jacobderynk.asterogram.data.network.response.AsteroidResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MeteoriteLandingsService {

    /**
     * [Meteorite Landings](https://dev.socrata.com/foundry/data.nasa.gov/y77d-th95)
     */
    @GET("gh4g-9sfh.json")
    suspend fun getAsteroids(
        @Query("\$order") order: String = "id",
        @Query("\$limit") limit: Int,
        @Query("\$offset") offset: Int,
    ): Response<List<AsteroidResponse>>

}