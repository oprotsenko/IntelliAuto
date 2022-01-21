package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetLocalMusic(get(), get())
    }

    factory {
        MediaPlayerCommandRunner(audioPlayer = get())
    }
}