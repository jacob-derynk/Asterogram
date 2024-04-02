package dev.jacobderynk.asterogram

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dev.jacobderynk.asterogram.di.daoModule
import dev.jacobderynk.asterogram.di.networkingModule
import dev.jacobderynk.asterogram.di.repositoryModule
import dev.jacobderynk.asterogram.di.dataStoreModule
import dev.jacobderynk.asterogram.di.databaseModule
import dev.jacobderynk.asterogram.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.Forest.plant


class AsterogramApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }

        // Plant also release tree here if you wanna implement Crashlytics

        startKoin {
            appContext = this@AsterogramApplication
            androidLogger(Level.ERROR)
            androidContext(this@AsterogramApplication)
            modules(
                networkingModule,
                repositoryModule,
                viewModelModule,
                dataStoreModule,
                databaseModule,
                daoModule,
            )
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
            private set
    }

}