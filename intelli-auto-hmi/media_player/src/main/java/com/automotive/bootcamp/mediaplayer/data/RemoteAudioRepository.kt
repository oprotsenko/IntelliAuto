package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.data.remote.RemoteMediaSource
import com.automotive.bootcamp.mediaplayer.domain.RemoteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteAudioRepository(
    private val remoteMediaSource: RemoteMediaSource,
    private val dispatcher: CoroutineDispatcher
) : RemoteMediaRepository {
    override suspend fun retrieveRemoteAudio(): List<Audio>? =
        withContext(dispatcher) {
            remoteMediaSource.retrieveRemoteMusic()?.map { audioItem ->
                audioItem.mapToAudio()
            }
        }
}