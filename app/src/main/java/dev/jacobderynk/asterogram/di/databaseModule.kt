package dev.jacobderynk.asterogram.di

import dev.jacobderynk.asterogram.AsterogramApplication
import dev.jacobderynk.asterogram.data.database.AsteroidDatabase
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(): AsteroidDatabase =
        AsteroidDatabase.getDatabase(AsterogramApplication.appContext)

    single { provideDatabase() }
}