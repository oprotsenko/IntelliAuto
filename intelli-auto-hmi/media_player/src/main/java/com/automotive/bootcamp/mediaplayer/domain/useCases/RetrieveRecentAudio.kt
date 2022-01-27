package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio

class RetrieveRecentAudio(
    private val recentAudioRepository: RecentAudioRepository
) {
    suspend fun retrieveRecentAudio() =
        recentAudioRepository.getPlaylist()?.list?.map {
            it.mapToAudio().wrapAudio()
        }
}