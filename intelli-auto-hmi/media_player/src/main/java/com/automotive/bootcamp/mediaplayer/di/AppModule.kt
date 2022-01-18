package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic
import com.automotive.bootcamp.mediaplayer.viewModels.MediaPlayerViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.SongsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        SongsListViewModel(get())
    }

    viewModel {
        NowPlayingViewModel(
            shuffleSongs = get(),
            previousSong = get(),
            playSong = get(),
            pauseSong = get(),
            nextSong = get(),
            repeatOneSong = get()
        )
    }
    single { GetLocalMusic(get()) }

    single { getContentResolver(get()) }
}

fun getContentResolver(context: Context) : ContentResolver =
    context.contentResolver