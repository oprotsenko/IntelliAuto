package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.data.RemoteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.remote.AudioAPI
import com.automotive.bootcamp.mediaplayer.data.remote.RemoteAudioSource
import com.automotive.bootcamp.mediaplayer.data.remote.retrofit.RetrofitAudioSource
import com.automotive.bootcamp.mediaplayer.domain.RemoteMediaRepository
import org.koin.dsl.module

val remoteModule = module {
    single { provideMyProfileRepository(get()) }
    single { provideRemoteDataSource(get()) }
}

fun provideMyProfileRepository(
    remote: RemoteAudioSource
) : RemoteMediaRepository = RemoteAudioRepository(remote)

fun provideRemoteDataSource(remoteData: AudioAPI) : RemoteAudioSource =
    RetrofitAudioSource(remoteData)