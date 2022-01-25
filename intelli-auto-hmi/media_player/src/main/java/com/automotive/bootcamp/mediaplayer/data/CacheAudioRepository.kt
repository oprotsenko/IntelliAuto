package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

class CacheAudioRepository (private val cacheAudioSource: CacheAudioSource) {
    suspend fun addAudio(audio: AudioItem): Long {
        return cacheAudioSource.insertAudio(audio)
    }

    suspend fun addAudios(audios: List<AudioItem>):List<Long> {
        return cacheAudioSource.insertAudios(audios)
    }
}