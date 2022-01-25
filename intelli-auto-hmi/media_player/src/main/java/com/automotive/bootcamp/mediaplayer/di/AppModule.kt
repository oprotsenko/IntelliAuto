package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.DefaultAudioPlayer
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.LocalMusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        LocalMusicViewModel(get(), get(), get())
    }

    viewModel {
        NowPlayingViewModel(playerCommandRunner = get())
    }

    single { provideDefaultAudioPlayer(get()) }
    single { MediaMetadataRetriever() }
    single { getContentResolver(context = get()) }
}

fun provideDefaultAudioPlayer(context: Context): AudioPlayer =
    DefaultAudioPlayer(context)

fun getContentResolver(context: Context): ContentResolver =
    context.contentResolver