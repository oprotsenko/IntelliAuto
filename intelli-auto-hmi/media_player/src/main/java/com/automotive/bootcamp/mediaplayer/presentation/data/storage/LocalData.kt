package com.automotive.bootcamp.mediaplayer.presentation.data.storage

import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum

interface LocalData {
    suspend fun getAlbums(): List<MediaAlbum>
}