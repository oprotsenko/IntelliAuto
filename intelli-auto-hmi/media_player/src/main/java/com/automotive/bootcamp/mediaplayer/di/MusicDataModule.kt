package com.automotive.bootcamp.mediaplayer.di

import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.data.CacheAudioRepository
import com.automotive.bootcamp.mediaplayer.data.LocalAudioRepository
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.cache.room.RoomAudioSource
import com.automotive.bootcamp.mediaplayer.data.localRepository.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.localRepository.resources.ResourcesAudioSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.DefaultAudioPlayer
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(localMusic = get()) }
    single { provideLocalMusicSource(get(), get()) }
    single { provideCacheRepository(cacheSource = get()) }
    single { provideCacheAudioSource(context = get())}
    single { provideDefaultAudioPlayer(get()) }
    single { MediaMetadataRetriever() }
    single { ResourcesAudioSource(get(), get()) }
}

fun provideMusicRepository(localMusic: LocalMedia): LocalMediaRepository =
    LocalAudioRepository(localMusic)

/**
To retrieve audio from external storage
 */
//fun provideLocalMusicSource(
//    contentResolver: ContentResolver,
//    retriever: MediaMetadataRetriever
//): LocalMedia =
//    LocalAudioSource(contentResolver, retriever)

/**
To retrieve audio from raw folder
 */
fun provideLocalMusicSource(
    retriever: MediaMetadataRetriever, context: Context
): LocalMedia =
    ResourcesAudioSource(retriever, context)

fun provideCacheRepository(cacheSource: CacheAudioSource): CacheAudioRepository =
    CacheAudioRepository(cacheSource)

fun provideCacheAudioSource(context: Context): CacheAudioSource =
    RoomAudioSource(context)

fun provideDefaultAudioPlayer(context: Context): AudioPlayer =
    DefaultAudioPlayer(context)