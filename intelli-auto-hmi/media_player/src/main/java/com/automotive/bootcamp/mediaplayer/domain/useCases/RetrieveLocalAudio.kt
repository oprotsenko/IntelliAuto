package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.CacheMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class RetrieveLocalAudio(
    private val repositoryLocal: LocalMediaRepository,
    private val cacheAudioRepository: CacheMediaRepository
) {
    suspend fun retrieveLocalMusic(): List<AudioWrapper> {
        val localMusic = repositoryLocal.retrieveLocalAudio()
        val ids = cacheAudioRepository.addAudios(localMusic)
        return localMusic.zip(ids) { audio: AudioItem, id: Long ->
            audio.copy(id = id)
        }.map { audio ->
            audio.mapToAudio().wrapAudio()
        }
    }
}