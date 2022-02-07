package com.automotive.bootcamp.music_browse_service.sources.remote.retrofit

import com.automotive.bootcamp.music_browse_service.AudioItem
import com.automotive.bootcamp.music_browse_service.sources.remote.AudioAPI
import com.automotive.bootcamp.music_browse_service.sources.remote.retrofit.responses.mapToAudioItem

class RetrofitAudioSource(private val audioAPI: AudioAPI) {
    suspend fun retrieveRemoteMusic(): List<AudioItem>? {
        val response = audioAPI.getRemoteMusic()
        return if (response.isSuccessful) {
            response.body()?.music?.map {
                it.mapToAudioItem()
            }
        } else null
    }
}