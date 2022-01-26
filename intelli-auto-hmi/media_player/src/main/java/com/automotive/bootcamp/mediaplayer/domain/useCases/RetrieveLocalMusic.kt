package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.CacheAudioRepository
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class RetrieveLocalMusic(
    private val repositoryLocal: LocalMediaRepository,
    private val cacheAudioRepository: CacheAudioRepository
) {

    suspend fun retrieveLocalMusic() : List<AudioItem> {
        val localMusic = repositoryLocal.retrieveLocalAudio()
        val ids = cacheAudioRepository.addAudios(localMusic)
        return localMusic.zip(ids) { audio: AudioItem, id: Long ->  
            audio.copy(id = id)
        }
    }
}