package com.automotive.bootcamp.mediaplayer.data.local

import com.automotive.bootcamp.mediaplayer.domain.models.Song

interface LocalMedia {
    suspend fun getLocalSongs(): List<Song>
}