package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.basicService.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.utils.ExoAudioPlayer
import com.automotive.bootcamp.mediaplayer.viewModels.*
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.LocalAudioViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.RecentAudioViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.OnlineAudioViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.PlaylistsViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.FavouriteAudioViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { NowPlayingViewModel(get()) }
    viewModel { FavouriteAudioViewModel(get(), get(), get(), get()) }
    viewModel { RecentAudioViewModel(get(), get(), get(), get()) }

    single { LocalAudioViewModel(get(), get(), get(), get()) }
    single { OnlineAudioViewModel(get(), get(), get(), get()) }
    single { CustomPlaylistViewModel(get(),get(), get()) }
    single { AudioPlayerService() }

    single { MediaMetadataRetriever() }
    single { provideAudioPlayer(get()) }
    single { getContentResolver(get()) }
}

fun provideAudioPlayer(context: Context): AudioPlayer =
    ExoAudioPlayer(context)

fun getContentResolver(context: Context): ContentResolver =
    context.contentResolver