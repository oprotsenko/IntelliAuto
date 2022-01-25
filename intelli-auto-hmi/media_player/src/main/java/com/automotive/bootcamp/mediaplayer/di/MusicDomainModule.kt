package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory { MediaPlayerCommandRunner(audioPlayer = get()) }

    single { AddRemoveFavourite(get()) }
    single { AddRemoveRecent(get()) }
    single { AddToPlaylist(get()) }
    single { CreatePlaylist(get()) }
    single { DeletePlaylist(get()) }
    single { RetrieveFavouriteMusic(get()) }
    single { RetrieveLocalMusic(get()) }
    single { RetrieveRecentAudio(get()) }
    single { RetrievePlaylists(get()) }
}