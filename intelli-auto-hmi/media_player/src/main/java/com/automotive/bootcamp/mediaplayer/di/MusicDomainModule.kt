package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetLocalMusic(get(), get())
    }
    factory { RetrieveLocalMusic(get()) }
    factory { AudioPlaybackControl(get()) }

    single { AddRemoveFavourite(get(), get()) }
    single { AddRemoveRecent(get(), get()) }
    single { RetrieveRecentAudio(get()) }
}