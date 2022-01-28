package com.automotive.bootcamp.mediaplayer.data.remote.retrofit

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.remote.AudioAPI
import com.automotive.bootcamp.mediaplayer.data.remote.RemoteAudioSource
import com.automotive.bootcamp.mediaplayer.data.remote.retrofit.responses.mapToAudioItem

class RetrofitAudioSource(private val audioAPI: AudioAPI) : RemoteAudioSource {
    override suspend fun retrieveRemoteMusic(): List<AudioItem>? {
        val response = audioAPI.getRemoteMusic()
        return if (response.isSuccessful) {
            response.body()?.music?.map {
                it.mapToAudioItem()
            }
        } else null
    }
}