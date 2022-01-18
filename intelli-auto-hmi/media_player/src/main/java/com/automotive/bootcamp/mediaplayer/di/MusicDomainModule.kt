package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetLocalMusic(get())
    }

    factory {
        PlaySong(musicPlayer = get())
    }
    factory {
        PauseSong(musicPlayer = get())
    }
    factory {
        NextSong(musicPlayer = get())
    }
    factory {
        PreviousSong(musicPlayer = get())
    }
    factory {
        ShuffleSongs(musicPlayer = get())
    }
    factory {
        RepeatOneSong()
    }
}