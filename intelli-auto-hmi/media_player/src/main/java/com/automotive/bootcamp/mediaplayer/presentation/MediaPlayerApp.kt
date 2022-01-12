package com.automotive.bootcamp.mediaplayer.presentation

import android.app.Application
import com.automotive.bootcamp.mediaplayer.presentation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MediaPlayerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MediaPlayerApp)
            modules(listOf(appModule))
        }
    }
}