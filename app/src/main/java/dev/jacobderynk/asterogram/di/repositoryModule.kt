package dev.jacobderynk.asterogram.di

import dev.jacobderynk.asterogram.data.repository.AsteroidRemoteRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { AsteroidRemoteRepositoryImpl(get()) }
}