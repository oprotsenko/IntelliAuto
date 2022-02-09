package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.CacheMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.RemoteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio

class RetrieveOnlineAudio(
    private val remoteAudioRepository: RemoteMediaRepository,
    private val cacheAudioRepository: CacheMediaRepository
) {
    suspend fun retrieveOnlineMusic(): List<Audio>? {
        val onlineMusic = remoteAudioRepository.retrieveRemoteAudio()
        val ids = onlineMusic?.let { cacheAudioRepository.addAudios(it) }
        return if (ids != null) {
            onlineMusic.zip(ids) { audio: Audio, id: Long ->
                audio.copy(id = id)
            }
        } else null
    }
}