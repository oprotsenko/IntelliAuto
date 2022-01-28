package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.remote.RemoteAudioSource
import com.automotive.bootcamp.mediaplayer.domain.RemoteMediaRepository

class RemoteAudioRepository(private val remoteAudioSource: RemoteAudioSource) :
    RemoteMediaRepository {
    override suspend fun retrieveRemoteAudio(): List<AudioItem>? =
        remoteAudioSource.retrieveRemoteMusic()
}