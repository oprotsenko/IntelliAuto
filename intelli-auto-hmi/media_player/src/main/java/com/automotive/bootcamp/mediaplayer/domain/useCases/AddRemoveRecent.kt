package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AddRemoveRecent(private val recentAudioRepository: RecentAudioRepository) {
    suspend fun addRemoveRecent(audio: List<AudioWrapper>?, position: Int): List<AudioWrapper>? {
        val list = audio?.toMutableList()
        if (list?.get(position)?.isRecent == true) {
            recentAudioRepository.removeAudio(list[position].audio.id)
        } else {
            list?.let { recentAudioRepository.addAudio(list[position].audio.id) }
        }
        return recentAudioRepository.getPlaylist()?.mapToPlaylist()?.list?.map {
            it.wrapAudio()
        }
    }
}