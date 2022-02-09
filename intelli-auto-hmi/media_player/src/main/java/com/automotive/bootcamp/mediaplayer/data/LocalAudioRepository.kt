package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.data.local.LocalMediaSource
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalAudioRepository(
    private val localAudioSource: LocalMediaSource,
    private val dispatcher: CoroutineDispatcher
) : LocalMediaRepository {

    override suspend fun retrieveLocalAudio(): List<Audio> =
        withContext(dispatcher) {
            localAudioSource.retrieveLocalAudio().map { audioItem ->
                audioItem.mapToAudio()
            }
        }
}