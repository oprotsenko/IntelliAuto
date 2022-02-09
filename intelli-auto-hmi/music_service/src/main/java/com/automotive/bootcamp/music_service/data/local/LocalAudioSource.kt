package com.automotive.bootcamp.music_service.data.local

import com.automotive.bootcamp.music_service.data.models.AudioItem

interface LocalAudioSource {
    suspend fun retrieveLocalAudio(): List<AudioItem>
}