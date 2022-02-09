package com.automotive.bootcamp.mediaplayer.di

import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.data.*
import com.automotive.bootcamp.mediaplayer.data.cache.CacheMediaSource
import com.automotive.bootcamp.mediaplayer.data.cache.room.RoomAudioSource
import com.automotive.bootcamp.mediaplayer.data.local.LocalMediaSource
import com.automotive.bootcamp.mediaplayer.data.local.resources.ResourcesAudioSource

import com.automotive.bootcamp.mediaplayer.domain.*

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    single { provideLocalMediaRepository(get(), get()) }
    single { provideCacheMediaRepository(get(), get()) }
    single { provideRecentMediaRepository(get(), get()) }
    single { provideFavouriteMediaRepository(get(), get()) }
    single { providePlaylistMediaRepository(get(), get()) }
    single { provideLocalAudioSource(get(), get()) }
    single { provideCacheAudioSource(get()) }
    single { ResourcesAudioSource(get(), get()) }

    factory { provideDispatcher() }
}

fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

fun provideLocalMediaRepository(
    localMusic: LocalMediaSource,
    dispatcher: CoroutineDispatcher
): LocalMediaRepository =
    LocalAudioRepository(localMusic, dispatcher)

fun provideCacheMediaRepository(
    cacheSource: CacheMediaSource,
    dispatcher: CoroutineDispatcher
): CacheMediaRepository =
    CacheAudioRepository(cacheSource, dispatcher)

fun provideRecentMediaRepository(
    cacheSource: CacheMediaSource,
    dispatcher: CoroutineDispatcher
): RecentMediaRepository =
    RecentAudioRepository(cacheSource, dispatcher)

fun provideFavouriteMediaRepository(
    cacheSource: CacheMediaSource,
    dispatcher: CoroutineDispatcher
): FavouriteMediaRepository =
    FavouriteAudioRepository(cacheSource, dispatcher)

fun providePlaylistMediaRepository(
    cacheSource: CacheMediaSource,
    dispatcher: CoroutineDispatcher
): PlaylistMediaRepository =
    PlaylistRepository(cacheSource, dispatcher)

/**
To retrieve audio from external storage
 */
//fun provideLocalAudioSource(
//    contentResolver: ContentResolver,
//    retriever: MediaMetadataRetriever,
//): LocalMedia =
//    LocalAudioSource(contentResolver, retriever)

/**
To retrieve audio from raw folder
 */
fun provideLocalAudioSource(
    retriever: MediaMetadataRetriever, context: Context
): LocalMediaSource =
    ResourcesAudioSource(retriever, context)

fun provideCacheAudioSource(context: Context): CacheMediaSource =
    RoomAudioSource(context)
