package com.automotive.bootcamp.music_service.data.remote

import com.automotive.bootcamp.music_service.data.models.AudioItem

interface RemoteAudioSource {
    suspend fun retrieveRemoteMusic(): List<AudioItem>?
}