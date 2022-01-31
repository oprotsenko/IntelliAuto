package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository

class ManageRecent(
    private val recentAudioRepository: RecentAudioRepository,
) {
    suspend fun removeAudio(aid: Long) {
        recentAudioRepository.removeAudio(aid)
    }

    suspend fun hasAudio(aid: Long): Boolean =
        recentAudioRepository.hasAudio(aid)
}