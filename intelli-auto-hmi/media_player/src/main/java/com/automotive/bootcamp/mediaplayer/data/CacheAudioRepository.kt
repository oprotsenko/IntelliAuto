package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.CacheMediaRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CacheAudioRepository(
    private val cacheAudioSource: CacheAudioSource,
    private val dispatcher: CoroutineDispatcher
) : CacheMediaRepository {
    override suspend fun addAudio(audio: AudioItem): Long =
        withContext(dispatcher) {
            cacheAudioSource.insertAudio(audio)
        }

    override suspend fun addAudios(audios: List<AudioItem>): List<Long> =
        withContext(dispatcher) {
            cacheAudioSource.insertAudios(audios)
        }
}