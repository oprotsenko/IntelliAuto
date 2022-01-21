package com.automotive.bootcamp.mediaplayer.data.localRepository

import com.automotive.bootcamp.mediaplayer.data.localRepository.models.AudioItem

interface LocalMedia {
    suspend fun getLocalSongs(): List<AudioItem>
}