package dev.jacobderynk.asterogram.di

import android.content.Context
import dev.jacobderynk.asterogram.data.datastore.DatastoreRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single { provideDataStoreRepository(androidContext()) }
}

fun provideDataStoreRepository(context: Context): DatastoreRepositoryImpl = DatastoreRepositoryImpl(context)