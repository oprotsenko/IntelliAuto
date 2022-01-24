package com.automotive.bootcamp.mediaplayer.di

import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.data.StorageAudioRepository
import com.automotive.bootcamp.mediaplayer.data.LocalAudioRepository
import com.automotive.bootcamp.mediaplayer.data.storage.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.storage.StorageMedia
import com.automotive.bootcamp.mediaplayer.data.storage.room.RoomAudioSource
import com.automotive.bootcamp.mediaplayer.data.local.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.local.resources.ResourcesAudioSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(localMusic = get()) }
    single { provideLocalMusicSource(get(), get()) }
    single { provideStorageRepository(cacheSource = get()) }
    single { provideCacheAudioSource(context = get())}
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

fun provideStorageRepository(cacheSource: CacheAudioSource): StorageMedia =
    StorageAudioRepository(cacheSource)

fun provideCacheAudioSource(context: Context): CacheAudioSource =
    RoomAudioSource(context)