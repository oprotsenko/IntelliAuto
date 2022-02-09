package com.automotive.bootcamp.music_service

import android.app.Application
import com.automotive.bootcamp.music_service.di.dataModule
import com.automotive.bootcamp.music_service.di.remoteModule
import com.automotive.bootcamp.music_service.di.retrofitModule
import com.automotive.bootcamp.music_service.di.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ServiceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ServiceApp)
            modules(
                listOf(
                    dataModule,
                    remoteModule,
                    retrofitModule,
                    serviceModule
                )
            )
        }
    }
}