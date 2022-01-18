package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.data.MusicRepository
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.MediaRepository
import com.automotive.bootcamp.mediaplayer.DefaultMusicPlayer
import com.automotive.bootcamp.mediaplayer.MusicPlayer
import com.automotive.bootcamp.mediaplayer.data.MusicRepository
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.MediaRepository
import android.content.ContentResolver
import com.automotive.bootcamp.mediaplayer.data.LocalMusicRepository
import com.automotive.bootcamp.mediaplayer.data.local.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.local.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import org.koin.dsl.module

val dataModule = module {

    single<MusicPlayer> {
        DefaultMusicPlayer()
    }
    single { provideMusicRepository(get()) }
    single { provideLocalMusicSource(get()) }
}

fun provideMusicRepository(localMusic: LocalMedia): LocalMediaRepository =
    LocalMusicRepository(localMusic)

fun provideLocalMusicSource(contentResolver: ContentResolver): LocalMedia =
    LocalMusicSource(contentResolver)