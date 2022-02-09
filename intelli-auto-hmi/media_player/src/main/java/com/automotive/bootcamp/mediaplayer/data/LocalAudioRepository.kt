package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.local.LocalMediaSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalAudioRepository(
    private val localAudioSource: LocalMediaSource,
    private val dispatcher: CoroutineDispatcher
) :
    LocalMediaRepository {

    override suspend fun retrieveLocalAudio(): List<AudioItem> =
        withContext(dispatcher) {
            localAudioSource.retrieveLocalAudio()
        }
}