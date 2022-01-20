package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import com.automotive.bootcamp.mediaplayer.data.LocalMusicRepository
import com.automotive.bootcamp.mediaplayer.data.local.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.local.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.utils.DefaultAudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import org.koin.dsl.module

val dataModule = module {
    single { provideMusicRepository(localMusic = get()) }
    single { provideLocalMusicSource(contentResolver = get()) }
    single { provideDefaultAudioPlayer() }
}

fun provideMusicRepository(localMusic: LocalMedia): LocalMediaRepository =
    LocalMusicRepository(localMusic)

fun provideLocalMusicSource(contentResolver: ContentResolver): LocalMedia =
    LocalMusicSource(contentResolver)

fun provideDefaultAudioPlayer(): AudioPlayer =
    DefaultAudioPlayer()