package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.viewModels.MediaPlayerViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.NowPlayingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MediaPlayerViewModel(getLocalMusic = get())
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
}