package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic
import com.automotive.bootcamp.mediaplayer.viewModels.MediaPlayerListingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MediaPlayerListingViewModel(get())
    }
    single {
        GetLocalMusic(get())
    }
}