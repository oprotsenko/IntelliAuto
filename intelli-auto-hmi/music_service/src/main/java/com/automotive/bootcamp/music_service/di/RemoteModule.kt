package com.automotive.bootcamp.music_service.di

import com.automotive.bootcamp.music_service.data.RemoteAudioRepository
import com.automotive.bootcamp.music_service.data.RemoteMediaRepository
import com.automotive.bootcamp.music_service.data.remote.AudioAPI
import com.automotive.bootcamp.music_service.data.remote.RemoteAudioSource
import com.automotive.bootcamp.music_service.data.remote.retrofit.RetrofitAudioSource
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

val remoteModule = module {
    single { provideRemoteRepository(get(), get()) }
    single { provideRemoteDataSource(get()) }
}

fun provideRemoteRepository(
    remote: RemoteAudioSource, dispatcher: CoroutineDispatcher
) : RemoteMediaRepository = RemoteAudioRepository(remote, dispatcher)

fun provideRemoteDataSource(remoteData: AudioAPI) : RemoteAudioSource =
    RetrofitAudioSource(remoteData)