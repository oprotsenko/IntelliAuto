package com.automotive.bootcamp.mediaplayer.data.local

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

interface LocalMediaSource {
    suspend fun retrieveLocalAudio(): List<AudioItem>
}