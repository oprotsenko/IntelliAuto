package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {

//    factory { MediaServiceControl(get()) }

    single { InitializeMediaPlayer(get(), get(), get()) }
    single { ManageFavourite(get()) }
    single { AddRecent(get(), get(), get()) }
    single { ManageRecent(get()) }
    single { DeletePlaylist(get()) }
    single { RetrieveFavouriteAudio(get()) }
    single { RetrieveLocalAudio(get(), get()) }
    single { RetrieveRecentAudio(get()) }
    single { RetrieveOnlineAudio(get(), get()) }
    single { ManagePlaylists(get()) }
}