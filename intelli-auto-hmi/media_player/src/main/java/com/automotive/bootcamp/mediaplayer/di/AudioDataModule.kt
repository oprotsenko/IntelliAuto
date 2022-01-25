package com.automotive.bootcamp.mediaplayer.di

import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.data.*
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.cache.room.RoomAudioSource
import com.automotive.bootcamp.mediaplayer.data.localRepository.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.localRepository.resources.ResourcesAudioSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import okhttp3.Cache
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(get()) }
    single { provideLocalMusicSource(get(), get()) }
    single { provideCacheAudioSource(get()) }
    single { provideCacheAudioRepository(get()) }
    single { provideRecentAudioRepository(get()) }
    single { provideFavouriteAudioRepository(get()) }
    single { providePlaylistRepository(get()) }
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

fun provideCacheAudioSource(context: Context): CacheAudioSource =
    RoomAudioSource(context)

fun provideCacheAudioRepository(cacheSource: CacheAudioSource): CacheAudioRepository =
    CacheAudioRepository(cacheSource)

fun provideRecentAudioRepository(cacheSource: CacheAudioSource): RecentAudioRepository =
    RecentAudioRepository(cacheSource)

fun provideFavouriteAudioRepository(cacheSource: CacheAudioSource): FavouriteAudioRepository =
    FavouriteAudioRepository(cacheSource)

fun providePlaylistRepository(cacheSource: CacheAudioSource): PlaylistRepository =
    PlaylistRepository(cacheSource)
