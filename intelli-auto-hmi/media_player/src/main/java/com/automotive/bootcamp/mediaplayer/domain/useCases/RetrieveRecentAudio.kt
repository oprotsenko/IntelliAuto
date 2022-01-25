package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

class RetrieveRecentAudio(private val recentAudioRepository: RecentAudioRepository) {
    suspend fun retrieveRecentAudio(): List<AudioItem>? {
        return recentAudioRepository.getPlaylist()?.list
    }
}