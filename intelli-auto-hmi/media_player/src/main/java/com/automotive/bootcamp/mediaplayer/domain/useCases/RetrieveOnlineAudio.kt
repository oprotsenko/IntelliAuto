package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.CacheMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.RemoteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class RetrieveOnlineAudio(
    private val remoteAudioRepository: RemoteMediaRepository,
    private val cacheAudioRepository: CacheMediaRepository
) {

    suspend fun retrieveOnlineMusic(): List<AudioWrapper>? {
        val onlineMusic = remoteAudioRepository.retrieveRemoteAudio()
        val ids = onlineMusic?.let { cacheAudioRepository.addAudios(it) }
        return if (ids != null) {
            onlineMusic.zip(ids) { audio: AudioItem, id: Long ->
                audio.copy(id = id)
            }.map { it.mapToAudio().wrapAudio() }
        } else null
    }
}