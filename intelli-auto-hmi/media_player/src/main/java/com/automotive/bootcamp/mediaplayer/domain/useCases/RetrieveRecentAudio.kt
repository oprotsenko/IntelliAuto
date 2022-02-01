package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RetrieveRecentAudio(private val recentAudioRepository: RecentMediaRepository) {
    fun retrieveRecentAudio(): Flow<List<AudioWrapper>?>? {
        return recentAudioRepository.getPlaylist()?.map { playlist ->
            playlist?.list?.map {
                it.mapToAudio().wrapAudio()
            }
        }
    }
}