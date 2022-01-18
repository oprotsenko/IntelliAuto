package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.DefaultMusicPlayer
import com.automotive.bootcamp.mediaplayer.MusicPlayer
import com.automotive.bootcamp.mediaplayer.data.MusicRepository
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.storage.LocalMusicSource
import com.automotive.bootcamp.mediaplayer.domain.MediaRepository
import org.koin.dsl.module

val dataModule = module {
    single<LocalMedia> {
        LocalMusicSource()
    }
    single<MediaRepository> {
        MusicRepository(localMedia = get())
    }
    single<MusicPlayer> {
        DefaultMusicPlayer()
    }
}