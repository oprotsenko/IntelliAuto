package com.automotive.bootcamp.mediaplayer.data.remote

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

interface RemoteAudioSource {
    suspend fun retrieveRemoteMusic(): List<AudioItem>?
}