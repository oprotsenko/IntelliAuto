package com.automotive.bootcamp.mediaplayer.di

import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.data.LocalAudioRepository
import com.automotive.bootcamp.mediaplayer.data.localRepository.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.localRepository.local.LocalAudioSource
import com.automotive.bootcamp.mediaplayer.data.localRepository.resources.ResourcesAudioSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(localMusic = get()) }
    single { provideLocalMusicSource(get(), get()) }
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