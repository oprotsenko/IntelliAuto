package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class RemoveRecent(
    private val recentAudioRepository: RecentAudioRepository,
) {
    suspend fun execute(audios: List<AudioWrapper>?, position: Int): List<AudioWrapper>? {
        val audioWrapped = audios?.get(position)

        audioWrapped?.let {
            val aid = audioWrapped.audio.id
            if (recentAudioRepository.hasAudio(aid)) {
                recentAudioRepository.removeAudio(aid)
            }
        }

        return recentAudioRepository.getPlaylist()?.mapToPlaylist()?.list?.map {
            it.wrapAudio()
        }
    }
}