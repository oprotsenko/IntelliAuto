package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RetrieveRecentAudio(private val recentAudioRepository: RecentMediaRepository) {
    fun retrieveRecentAudio(): Flow<List<Audio>?>? {
        return recentAudioRepository.getPlaylist()?.map { playlist ->
            playlist?.list
        }
    }
}