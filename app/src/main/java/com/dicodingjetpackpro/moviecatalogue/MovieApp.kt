package com.dicodingjetpackpro.moviecatalogue

import android.app.Application
import com.dicodingjetpackpro.moviecatalogue.data.di.dataModule
import com.dicodingjetpackpro.moviecatalogue.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by aydbtiko on 5/6/2021.
 *
 */
class MovieApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidContext(this@MovieApp)
            modules(appModule, dataModule)
        }
        // logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
