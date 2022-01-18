package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.Song

interface LocalMediaRepository {
    suspend fun getLocalSongs(): List<Song>
}