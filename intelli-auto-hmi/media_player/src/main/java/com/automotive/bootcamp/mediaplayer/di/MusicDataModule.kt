package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.data.MusicRepository
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.MediaRepository
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(get()) }
    single { provideLocalMusicSource() }
}

fun provideMusicRepository(localMusic: LocalMedia): MediaRepository =
    MusicRepository(localMusic)

fun provideLocalMusicSource(): LocalMedia =
    LocalMusicSource()