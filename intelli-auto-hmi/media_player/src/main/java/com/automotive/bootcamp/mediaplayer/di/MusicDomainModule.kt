package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory { AudioPlaybackControl(get()) }

    single { AddRemoveFavourite(get(), get()) }
    single { AddRecent(get(), get()) }
    single { RemoveRecent(get()) }
    single { AddToPlaylist(get()) }
    single { CreatePlaylist(get()) }
    single { DeletePlaylist(get()) }
    single { RetrieveFavouriteMusic(get()) }
    single { RetrieveLocalMusic(get(), get()) }
    single { RetrieveRecentAudio(get()) }
    single { RetrievePlaylistAudio(get()) }
}