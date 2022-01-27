package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.DefaultAudioPlayer
import com.automotive.bootcamp.mediaplayer.viewModels.*
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.LocalMusicViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.RecentAudioViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.OnlineMusicViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.PlaylistsViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.FavouriteMusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LocalMusicViewModel(get(), get(), get(), get()) }
    viewModel { FavouriteMusicViewModel(get(), get(), get(), get()) }
    viewModel { OnlineMusicViewModel(get(), get(), get(), get()) }
    viewModel { PlaylistsViewModel(get(), get(), get()) }
    viewModel { NowPlayingViewModel(get(), get()) }
    viewModel { RecentAudioViewModel(get(), get(), get(), get()) }
    viewModel { CustomPlaylistViewModel(get(),get(), get()) }

    single { provideDefaultAudioPlayer(get()) }
    single { MediaMetadataRetriever() }
    single { getContentResolver(get()) }
}

fun provideDefaultAudioPlayer(context: Context): AudioPlayer =
    DefaultAudioPlayer(context)

fun getContentResolver(context: Context): ContentResolver =
    context.contentResolver