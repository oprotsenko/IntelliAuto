package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.models.AudioItem

interface RemoteMediaRepository {
    suspend fun retrieveRemoteAudio(): List<AudioItem>?
}