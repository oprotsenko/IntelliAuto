package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository

class ManageRecent(
    private val recentAudioRepository: RecentMediaRepository,
) {
    suspend fun removeAudio(aid: Long) {
        recentAudioRepository.removeAudio(aid)
    }

    suspend fun hasAudio(aid: Long): Boolean =
        recentAudioRepository.hasAudio(aid)
}