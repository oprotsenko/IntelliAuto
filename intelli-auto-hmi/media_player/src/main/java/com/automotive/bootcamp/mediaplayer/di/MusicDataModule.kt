package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import com.automotive.bootcamp.mediaplayer.data.LocalMusicRepository
import com.automotive.bootcamp.mediaplayer.data.local.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.local.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(get()) }
    single { provideLocalMusicSource(get()) }
}

fun provideMusicRepository(localMusic: LocalMedia): LocalMediaRepository =
    LocalMusicRepository(localMusic)

fun provideLocalMusicSource(contentResolver: ContentResolver): LocalMedia =
    LocalMusicSource(contentResolver)