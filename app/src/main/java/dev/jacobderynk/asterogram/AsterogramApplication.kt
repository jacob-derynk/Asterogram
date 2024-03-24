package dev.jacobderynk.asterogram

import android.app.Application
import dev.jacobderynk.asterogram.di.networkingModule
import dev.jacobderynk.asterogram.di.repositoryModule
//import dev.jacobderynk.asterogram.di.viewModelModule
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

        Timber.i("Application onCreate")

        // Plant also release tree here if you wanna implement Crashlytics

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AsterogramApplication)
            modules(
                networkingModule,
                repositoryModule,
                //viewModelModule
            )
        }
    }

}