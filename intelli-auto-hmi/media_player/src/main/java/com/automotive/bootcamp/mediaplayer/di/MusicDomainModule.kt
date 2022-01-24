package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory { RetrieveLocalMusic(get()) }
    factory { MediaPlayerCommandRunner(audioPlayer = get()) }

    single { AddRemoveFavourite() }
    single { AddRemoveRecent() }
}