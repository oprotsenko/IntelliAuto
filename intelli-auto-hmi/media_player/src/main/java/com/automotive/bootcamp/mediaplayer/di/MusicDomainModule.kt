package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory { AudioPlaybackControl(get()) }

    single { ManageFavourite(get(), get()) }
    single { AddRecent(get(), get()) }
    single { ManageRecent(get()) }
    single { DeletePlaylist(get()) }
    single { RetrieveFavouriteMusic(get()) }
    single { RetrieveLocalMusic(get(), get()) }
    single { RetrieveRecentAudio(get()) }
    single { RetrieveOnlineMusic(get(), get()) }
    single { RetrievePlaylistAudio(get()) }
    single { ManagePlaylists(get()) }
}