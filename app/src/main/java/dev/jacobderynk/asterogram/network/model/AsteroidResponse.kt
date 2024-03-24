package dev.jacobderynk.asterogram.network.model

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class AsteroidResponse(
    val id: String,
    val name: String,
    @Json(name="nametype") val nameType: String,
    @Json(name="recclass") val recClass: String,
    val mass: String,
    val fall: String,
    val year: String,
    @Json(name="reclat") val recLat: String,
    @Json(name="reclong") val recLong: String,
    val geolocation: GeolocationResponse,
) {
    @Serializable
    data class GeolocationResponse(
        val latitude: String,
        val longitude: String
    )
}