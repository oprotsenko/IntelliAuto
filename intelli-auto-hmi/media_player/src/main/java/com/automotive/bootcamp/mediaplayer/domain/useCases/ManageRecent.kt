package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class ManageRecent(
    private val recentAudioRepository: RecentAudioRepository,
) {
    suspend fun removeAudio(aid: Long) {
        recentAudioRepository.removeAudio(aid)
    }

    suspend fun hasAudio(aid: Long): Boolean =
        recentAudioRepository.hasAudio(aid)
}