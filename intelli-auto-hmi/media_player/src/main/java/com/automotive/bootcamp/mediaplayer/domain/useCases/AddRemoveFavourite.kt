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
//            list[position] = list[position].copy(isFavourite = false)
        } else {
            list?.get(position)?.audio?.id?.let { favouriteAudioRepository.addAudio(it) }
//            list?.set(position, list[position].copy(isFavourite = true))
        }
        val newList = favouriteAudioRepository.getPlaylist()?.mapToPlaylist()?.list?.map {
            it.wrapAudio()
        }
        return newList
    }
}