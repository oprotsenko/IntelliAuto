package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.models.AudioItem

interface LocalMediaRepository {
    suspend fun retrieveLocalAudio(): List<AudioItem>
}