package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio


class RetrieveFavouriteMusic(private val favouriteAudioRepository: FavouriteAudioRepository) {

    suspend fun retrieveFavouriteMusic() =
        favouriteAudioRepository.getPlaylist()?.list?.map { audio ->
            audio.mapToAudio().wrapAudio()
        }
}