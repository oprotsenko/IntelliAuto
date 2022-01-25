package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.CacheAudioRepository
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToAudioItem
import com.automotive.bootcamp.mediaplayer.domain.models.Audio

class GetLocalMusic(
    private val repositoryLocal: LocalMediaRepository,
    private val cacheAudioRepository: CacheAudioRepository
) {
    suspend fun getLocalSongs() = repositoryLocal.retrieveLocalAudio()

    suspend fun insertAudio(audio: Audio) {
        cacheAudioRepository.addAudio(audio.mapToAudioItem())
    }

    suspend fun insertAudios(audios: List<Audio>) {
        var mappedAudios = audios.map {
            it.mapToAudioItem()
        }

        cacheAudioRepository.addAudios(mappedAudios)
    }
}