package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AddRemoveRecent() {
    fun addRemoveRecent(audio: List<AudioWrapper>?, position: Int): List<AudioWrapper>? {
        val list = audio?.toMutableList()
        if (list?.get(position)?.isRecent == true) {
            list[position] = list[position].copy(isRecent = false)
        } else {
            list?.set(position, list[position].copy(isRecent = true))
        }
        return list
    }
}