package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.CacheMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class RetrieveLocalAudio(
    private val repositoryLocal: LocalMediaRepository,
    private val cacheAudioRepository: CacheMediaRepository
) {
    suspend fun retrieveLocalMusic(): List<Audio> {
        val localMusic = repositoryLocal.retrieveLocalAudio()
        val ids = cacheAudioRepository.addAudios(localMusic)
        return localMusic.zip(ids) { audio: Audio, id: Long ->
            audio.copy(id = id)
        }
    }

    fun getCachedAudio(pid: Long): Flow<List<Audio>?> =
        cacheAudioRepository.getAudios(pid).map { playlist ->
            playlist?.list
        }
}