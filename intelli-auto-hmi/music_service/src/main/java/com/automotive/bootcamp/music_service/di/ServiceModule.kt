package com.automotive.bootcamp.music_service.di

import com.automotive.bootcamp.music_service.data.CacheMediaRepository
import com.automotive.bootcamp.music_service.data.LocalMediaRepository
import com.automotive.bootcamp.music_service.data.ServiceSources
import com.automotive.bootcamp.music_service.data.cache.CacheRepository
import com.automotive.bootcamp.music_service.data.cache.room.RoomAudioSource
import com.automotive.bootcamp.music_service.data.local.LocalAudioSource
import com.automotive.bootcamp.music_service.data.local.LocalRepository
import com.automotive.bootcamp.music_service.data.remote.RemoteAudioSource
import org.koin.dsl.module

val serviceModule = module {
    single { LocalAudioSource(get()) }
    single { RemoteAudioSource() }
    single { ServiceSources() }
    single { RoomAudioSource(get()) }
    single { provideLocalMediaRepository(get()) }
    single { provideCacheMediaRepository(get()) }
}

fun provideLocalMediaRepository(localAudioSource: LocalAudioSource): LocalMediaRepository =
    LocalRepository(localAudioSource)

fun provideCacheMediaRepository(roomAudioSource: RoomAudioSource): CacheMediaRepository =
    CacheRepository(roomAudioSource)