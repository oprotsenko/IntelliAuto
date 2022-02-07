package com.automotive.bootcamp.music_browse_service.di

import android.media.MediaMetadataRetriever
import org.koin.dsl.module

val appModule = module {
    single { MediaMetadataRetriever() }
}
