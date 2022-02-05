package com.automotive.bootcamp.music_service.di

import android.content.Context
import com.automotive.bootcamp.music_service.service.sources.MusicSourceService
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import org.koin.dsl.module

val serviceModule = module {
   factory { MusicSourceService(get(), get(), get(), get(), get()) }
    single { provideAudioAttributes() }
    single { provideExoPlayer(get(), get()) }
    single { provideDataSourceFactory(get()) }
//    single { provideMusicServiceConnection(get()) }
}

fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
    .setContentType(C.CONTENT_TYPE_MUSIC)
    .setUsage(C.USAGE_MEDIA)
    .build()

fun provideExoPlayer(context: Context, audioAttributes: AudioAttributes): ExoPlayer =
    ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true)
    }

fun provideDataSourceFactory(context: Context): DefaultDataSource.Factory =
    DefaultDataSource.Factory(context)

//fun provideMusicServiceConnection(context: Context): com.automotive.bootcamp.mediaplayer.MusicServiceConnection =
//    com.automotive.bootcamp.mediaplayer.MusicServiceConnection(context)