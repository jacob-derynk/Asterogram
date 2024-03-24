package dev.jacobderynk.asterogram.data.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.jacobderynk.asterogram.data.model.CommunicationError
import dev.jacobderynk.asterogram.data.model.CommunicationResult
import dev.jacobderynk.asterogram.data.model.ErrorResponse
import retrofit2.Response

interface IBaseRepository {

    /**
     * Takes retrofits [Response] and converts it to [CommunicationResult] so we can seamlessly work
     * with APIs responses
     *
     * @return [CommunicationResult] according to the response type from the API
     */
    fun <T : Any> processResponse(call: Response<T>): CommunicationResult<T> {

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(ErrorResponse::class.java)

        return try {
            if (call.isSuccessful) {
                call.body()?.let {
                    return CommunicationResult.Success(it)
                } ?: kotlin.run {
                    return CommunicationResult.Error(
                        CommunicationError(
                            call.code(),
                            jsonAdapter.fromJson(
                                call.errorBody().toString(),
                            )!! // FIXME: better solution than !! needed
                        )
                    )
                }
            } else {
                CommunicationResult.Error(
                    CommunicationError(
                        call.code(),
                        jsonAdapter.fromJson(call.body().toString())!! // FIXME: better solution than !! needed
                    )
                )
            }

        } catch (e: Exception) {
            CommunicationResult.Exception(e)
        }
    }
}
