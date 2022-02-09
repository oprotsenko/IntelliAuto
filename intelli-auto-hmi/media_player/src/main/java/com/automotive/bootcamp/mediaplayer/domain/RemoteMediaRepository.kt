package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.Audio

interface RemoteMediaRepository {
    suspend fun retrieveRemoteAudio(): List<Audio>?
}