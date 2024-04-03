package dev.jacobderynk.asterogram.data.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable


@JsonClass(generateAdapter = true)
@Serializable
data class AsteroidResponse(
    val id: String,
    val name: String,
    @Json(name="nametype") val nameType: String = "",
    @Json(name="recclass") val recClass: String = "",
    val mass: String= "",
    val fall: String= "",
    val year: String= "",
    @Json(name="reclat") val recLat: String= "",
    @Json(name="reclong") val recLong: String= "",
    val geolocation: GeolocationResponse = GeolocationResponse("", ""),
) {
    @Serializable
    data class GeolocationResponse(
        val latitude: String = "",
        val longitude: String = "",
    )
}