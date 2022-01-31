package com.automotive.bootcamp.mediaplayer.data.remote

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.remote.retrofit.responses.RemoteMusicResponse
import retrofit2.Response

interface RemoteAudioSource {
    suspend fun retrieveRemoteMusic(): List<AudioItem>?
}