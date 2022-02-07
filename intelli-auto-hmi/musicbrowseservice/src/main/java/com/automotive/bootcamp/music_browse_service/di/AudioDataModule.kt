package com.automotive.bootcamp.music_browse_service.di

import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.music_browse_service.AbstractAudioSource
import com.automotive.bootcamp.music_browse_service.data.ResourcesAudioSource
import org.koin.dsl.module

val dataModule = module {
    single { provideLocalAudioSource(get(), get()) }
}

fun provideLocalAudioSource(
    retriever: MediaMetadataRetriever, context: Context
): AbstractAudioSource =
    ResourcesAudioSource(retriever, context)