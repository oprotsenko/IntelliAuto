package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.data.local.model.SongItem

interface LocalMediaRepository {
    suspend fun getLocalSongs(): List<SongItem>
}