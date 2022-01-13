package com.automotive.bootcamp.mediaplayer.presentation.di

import com.automotive.bootcamp.mediaplayer.presentation.MediaPlayerListingViewModel
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaRepository
import com.automotive.bootcamp.mediaplayer.presentation.data.storage.LocalData
import com.automotive.bootcamp.mediaplayer.presentation.data.storage.LocalDataSource
import com.automotive.bootcamp.mediaplayer.presentation.domain.AlbumsRepository
import com.automotive.bootcamp.mediaplayer.presentation.domain.useCases.GetLocalMusic
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MediaPlayerListingViewModel(get())
    }
    single {
        GetLocalMusic(get())
    }
    single {
        provideMusicRepository(get())
    }
    single {
        provideLocalDataSource()
    }
}

fun provideMusicRepository(localMusic: LocalData): AlbumsRepository =
    MediaRepository(localMusic)

fun provideLocalDataSource(): LocalData =
    LocalDataSource()