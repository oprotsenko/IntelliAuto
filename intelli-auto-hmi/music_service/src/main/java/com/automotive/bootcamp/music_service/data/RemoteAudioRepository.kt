package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.data.remote.RemoteAudioSource
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