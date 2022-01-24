package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AddRemoveFavourite() {
    fun addRemoveFavourite(audio: List<AudioWrapper>?, position: Int): List<AudioWrapper>? {
        val list = audio?.toMutableList()
        if (list?.get(position)?.isFavourite == true) {
            list[position] = list[position].copy(isFavourite = false)
        } else {
            list?.set(position, list[position].copy(isFavourite = true))
        }
        return list
    }
}