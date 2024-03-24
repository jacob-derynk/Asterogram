package dev.jacobderynk.asterogram.data.model

import kotlinx.serialization.Serializable

/**
 * Every network request should return this sealed class from the repository.
 */
sealed class CommunicationResult<out T : Any> {
    class Success<out T : Any>(val data: T) : CommunicationResult<T>()
    class Error(val error: CommunicationError) : CommunicationResult<Nothing>()
    class Exception(val exception: Throwable) : CommunicationResult<Nothing>()
}

@Serializable
data class CommunicationError(
    val responseCode: Int,
    val errorResponse: ErrorResponse,
)

@Serializable
data class ErrorResponse(
    /**
     * Class of the error, has nothing to do with the response code
     */
    val code: String,
    /**
     * Probably always true
     */
    val error: Boolean,
    /**
     * Human readable error message
     */
    val message: String,
    /**
     * Machine readable data
     */
    val data: CommunicationErrorData,
) {
    @Serializable
    data class CommunicationErrorData(val query: String)
}