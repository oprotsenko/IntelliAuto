package com.automotive.bootcamp.mediaplayer.di

import android.content.ContentResolver
import android.content.Context
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic
import com.automotive.bootcamp.mediaplayer.viewModels.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.LocalMusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        LocalMusicViewModel(getLocalMusic = get())
    }

    viewModel {
        NowPlayingViewModel(playerCommandRunner = get())
    }

    single { GetLocalMusic( get()) }
    single { getContentResolver(get()) }
}

fun getContentResolver(context: Context) : ContentResolver =
    context.contentResolver