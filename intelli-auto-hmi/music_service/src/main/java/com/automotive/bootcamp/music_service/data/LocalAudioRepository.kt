package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.local.LocalAudioSource
import com.automotive.bootcamp.music_service.data.models.AudioItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalAudioRepository(
    private val localAudioSource: LocalAudioSource,
    private val dispatcher: CoroutineDispatcher
) :
    LocalMediaRepository {

    override suspend fun retrieveLocalAudio(): List<AudioItem> =
        withContext(dispatcher) {
            localAudioSource.retrieveLocalAudio()
        }
}