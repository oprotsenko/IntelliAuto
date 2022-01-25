package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository


class RetrieveFavouriteMusic(private val favouriteAudioRepository: FavouriteAudioRepository) {

    suspend fun retrieveFavouriteMusic() = favouriteAudioRepository.getPlaylist()
}