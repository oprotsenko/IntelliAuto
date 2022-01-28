package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

interface RemoteMediaRepository {
    suspend fun retrieveRemoteAudio(): List<AudioItem>?
}