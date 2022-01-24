package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.data.localRepository.models.AudioItem

interface LocalMediaRepository {
    suspend fun retrieveLocalAudio(): List<AudioItem>
}