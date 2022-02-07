package com.automotive.bootcamp.music_browse_service.di

import com.automotive.bootcamp.music_browse_service.AbstractAudioSource
import com.automotive.bootcamp.music_browse_service.data.RetrofitAudioSource
import com.automotive.bootcamp.music_browse_service.data.remote.AudioAPI
import org.koin.dsl.module

val remoteModule = module {
    single { provideRemoteDataSource(get()) }
}

fun provideRemoteDataSource(remoteData: AudioAPI) : AbstractAudioSource =
    RetrofitAudioSource(remoteData)