package com.automotive.bootcamp.mediaplayer.data.local

import com.automotive.bootcamp.mediaplayer.data.local.model.SongItem

interface LocalMedia {
    suspend fun getLocalSongs(): List<SongItem>
}