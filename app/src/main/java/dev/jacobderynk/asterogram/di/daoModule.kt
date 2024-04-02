package dev.jacobderynk.asterogram.di

import dev.jacobderynk.asterogram.data.database.AsteroidDao
import dev.jacobderynk.asterogram.data.database.AsteroidDatabase
import dev.jacobderynk.asterogram.data.database.LikedAsteroidDao
import org.koin.dsl.module

val daoModule = module {
    fun provideAsteroidDao(database: AsteroidDatabase): AsteroidDao =
        database.asteroidDao()

    fun provideLikedAsteroidDao(database: AsteroidDatabase): LikedAsteroidDao =
        database.likedAsteroidDao()

    single { provideAsteroidDao(get()) }
    single { provideLikedAsteroidDao(get()) }
}