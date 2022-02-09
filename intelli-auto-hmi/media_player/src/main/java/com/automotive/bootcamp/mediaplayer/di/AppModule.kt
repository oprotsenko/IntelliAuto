package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.ExoAudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.basicService.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.viewModels.*
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.NowPlayingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { NowPlayingViewModel(get(), get(), get()) }
    viewModel { FavouriteAudioViewModel(get(), get(), get(), get()) }
    viewModel { RecentAudioViewModel(get(), get(), get(), get()) }

    viewModel { LocalAudioViewModel(get(), get(), get(), get()) }
    viewModel { OnlineAudioViewModel(get(), get(), get(), get()) }
    viewModel { CustomPlaylistViewModel(get(),get(), get()) }
    single { AudioPlayerService() }

    single { MediaMetadataRetriever() }
    single { provideAudioPlayer(get()) }
    single { getContentResolver(get()) }
//    single { MediaServiceControl(get()) }
//    single { MusicServiceConnection(get()) }
}

fun provideAudioPlayer(context: Context): AudioPlayer =
    ExoAudioPlayer(context)

fun getContentResolver(context: Context): ContentResolver =
    context.contentResolver