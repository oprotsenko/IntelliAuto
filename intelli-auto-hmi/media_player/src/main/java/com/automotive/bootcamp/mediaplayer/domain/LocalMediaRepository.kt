package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.Audio

interface LocalMediaRepository {
    suspend fun retrieveLocalAudio(): List<Audio>
}