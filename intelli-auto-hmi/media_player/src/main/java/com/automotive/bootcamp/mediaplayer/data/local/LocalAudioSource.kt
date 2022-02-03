package com.automotive.bootcamp.mediaplayer.data.local

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

interface LocalAudioSource {
    suspend fun retrieveLocalAudio(): List<AudioItem>
}