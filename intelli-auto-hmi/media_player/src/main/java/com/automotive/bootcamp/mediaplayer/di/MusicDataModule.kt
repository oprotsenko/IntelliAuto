package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import com.automotive.bootcamp.mediaplayer.data.LocalMusicRepository
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioRepository
import com.automotive.bootcamp.mediaplayer.data.cache.sources.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.cache.sources.RoomAudioSource
import com.automotive.bootcamp.mediaplayer.data.local.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.local.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.utils.DefaultAudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(localMusic = get()) }
    single { provideLocalMusicSource(contentResolver = get()) }
    single { provideCacheAudioRepository(cacheAudioSource = get()) }
    single { provideRoomAudioSource(context = get()) }
    single { provideDefaultAudioPlayer() }
}

fun provideMusicRepository(localMusic: LocalMedia): LocalMediaRepository =
    LocalMusicRepository(localMusic)

fun provideLocalMusicSource(contentResolver: ContentResolver): LocalMedia =
    LocalMusicSource(contentResolver)

fun provideCacheAudioRepository(cacheAudioSource: CacheAudioSource): CacheAudioRepository =
    CacheAudioRepository(cacheAudioSource)

fun provideRoomAudioSource(context: Context): CacheAudioSource =
    RoomAudioSource(context)

fun provideDefaultAudioPlayer(): AudioPlayer =
    DefaultAudioPlayer()