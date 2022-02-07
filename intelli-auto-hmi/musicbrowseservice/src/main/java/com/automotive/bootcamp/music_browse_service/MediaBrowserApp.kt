package com.automotive.bootcamp.music_browse_service

import android.app.Application
import com.automotive.bootcamp.music_browse_service.di.appModule
import com.automotive.bootcamp.music_browse_service.di.dataModule
import com.automotive.bootcamp.music_browse_service.di.remoteModule
import com.automotive.bootcamp.music_browse_service.di.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MediaBrowserApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MediaBrowserApp)
            modules(
                listOf(
                    appModule,
                    dataModule,
                    retrofitModule,
                    remoteModule
                )
            )
        }
    }
}