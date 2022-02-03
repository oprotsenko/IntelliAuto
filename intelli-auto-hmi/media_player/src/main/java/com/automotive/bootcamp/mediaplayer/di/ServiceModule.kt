package com.automotive.bootcamp.mediaplayer.di

import android.content.Context
import com.automotive.bootcamp.mediaplayer.service.MusicSource
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import org.koin.dsl.module

val serviceModule = module {
    single { MusicSource(get(), get(), get(), get(), get()) }
    single { provideAudioAttributes() }
    single { provideExoPlayer(get(), get()) }
    single { provideDataSourceFactory(get()) }
}

fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
    .setContentType(C.CONTENT_TYPE_MUSIC)
    .setUsage(C.USAGE_MEDIA)
    .build()

fun provideExoPlayer(context: Context, audioAttributes: AudioAttributes): Player =
    ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true)
    }

fun provideDataSourceFactory(context: Context): DefaultDataSource.Factory =
    DefaultDataSource.Factory(context)
