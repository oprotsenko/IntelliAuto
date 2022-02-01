package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory { AudioPlaybackControl(get()) }

    single { ManageFavourite(get(), get()) }
    single { AddRecent(get(), get()) }
    single { ManageRecent(get()) }
    single { DeletePlaylist(get()) }
    single { RetrieveFavouriteAudio(get()) }
    single { RetrieveLocalAudio(get(), get()) }
    single { RetrieveRecentAudio(get()) }
    single { RetrieveOnlineAudio(get(), get()) }
    single { ManagePlaylists(get()) }
}