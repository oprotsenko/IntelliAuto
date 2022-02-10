package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import com.automotive.bootcamp.mediaplayer.utils.AudioPlaybackControl
import com.automotive.bootcamp.mediaplayer.utils.player.AudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.player.ExoAudioPlayer
import com.automotive.bootcamp.mediaplayer.utils.basicService.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.viewModels.*
import com.automotive.bootcamp.mediaplayer.viewModels.NowPlayingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { FavouriteAudioViewModel(get(), get(), get(), get()) }
    viewModel { RecentAudioViewModel(get(), get(), get(), get()) }
    viewModel { LocalAudioViewModel(get(), get(), get(), get()) }
    viewModel { OnlineAudioViewModel(get(), get(), get(), get()) }
    viewModel { CustomPlaylistViewModel(get(), get(), get()) }
    viewModel { NowPlayingViewModel(get()) }
    viewModel { QuickPlaybackControlsViewModel(get()) }
//    single { AudioPlayerService() }

    single { AudioPlaybackControl(get(), get(), get()) }
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