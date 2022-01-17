package com.automotive.bootcamp.mediaplayer.data.storage

import com.automotive.bootcamp.mediaplayer.domain.models.Song

interface LocalMedia {
    suspend fun getAlbums(): List<Song>
}