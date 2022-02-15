package com.automotive.bootcamp.music_service.data.local

import com.automotive.bootcamp.music_service.data.LocalMediaRepository
import com.automotive.bootcamp.music_service.data.models.AudioItem

class LocalRepository(private val localAudioSource: LocalAudioSource): LocalMediaRepository {
    override suspend fun retrieveLocalAudio(): List<AudioItem> =
        localAudioSource.retrieveLocalAudio()
}