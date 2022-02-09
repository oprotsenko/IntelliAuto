package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.data.RemoteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.remote.AudioAPI
import com.automotive.bootcamp.mediaplayer.data.remote.RemoteMediaSource
import com.automotive.bootcamp.mediaplayer.data.remote.retrofit.RetrofitAudioSource
import com.automotive.bootcamp.mediaplayer.domain.RemoteMediaRepository
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

val remoteModule = module {
    single { provideRemoteRepository(get(), get()) }
    single { provideRemoteDataSource(get()) }
}

fun provideRemoteRepository(
    remote: RemoteMediaSource, dispatcher: CoroutineDispatcher
) : RemoteMediaRepository = RemoteAudioRepository(remote, dispatcher)

fun provideRemoteDataSource(remoteData: AudioAPI) : RemoteMediaSource =
    RetrofitAudioSource(remoteData)