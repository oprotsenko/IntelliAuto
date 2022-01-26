package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AddRemoveFavourite(private val favouriteAudioRepository: FavouriteAudioRepository) {
    suspend fun addRemoveFavourite(audio: List<AudioWrapper>?, position: Int): List<AudioWrapper>? {
        val list = audio?.toMutableList()
        if (list?.get(position)?.isFavourite == true) {
            favouriteAudioRepository.removeAudio(list[position].audio.id)
        } else {
            list?.let { favouriteAudioRepository.addAudio(list[position].audio.id) }
        }
        return favouriteAudioRepository.getPlaylist()?.mapToPlaylist()?.list?.map {
            it.wrapAudio()
        }
    }
}