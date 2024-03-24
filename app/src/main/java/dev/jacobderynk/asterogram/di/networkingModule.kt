package dev.jacobderynk.asterogram.di


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.jacobderynk.asterogram.BuildConfig
import dev.jacobderynk.asterogram.network.MeteoriteLandingsService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://data.nasa.gov/resource/"

val networkingModule = module {

    val loggingInterceptor = HttpLoggingInterceptor()

    val defaultHeaderInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request()
                .newBuilder()
                .header("X-App-Token", BuildConfig.APP_TOKEN)
                .build()
        )
    }

    val client: OkHttpClient = if (BuildConfig.DEBUG) {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(defaultHeaderInterceptor)
            .build()
    } else {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(defaultHeaderInterceptor)
            .build()
    }

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    fun provideApi(retrofit: Retrofit): MeteoriteLandingsService = retrofit.create(MeteoriteLandingsService::class.java)

    single {
        provideApi(getRetrofit())
    }
}