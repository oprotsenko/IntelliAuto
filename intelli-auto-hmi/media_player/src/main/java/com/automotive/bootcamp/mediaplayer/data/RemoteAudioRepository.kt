package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.remote.RemoteAudioSource
import com.automotive.bootcamp.mediaplayer.domain.RemoteMediaRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteAudioRepository(
    private val remoteAudioSource: RemoteAudioSource,
    private val dispatcher: CoroutineDispatcher
) :
    RemoteMediaRepository {
    override suspend fun retrieveRemoteAudio(): List<AudioItem>? =
        withContext(dispatcher) { remoteAudioSource.retrieveRemoteMusic() }
}