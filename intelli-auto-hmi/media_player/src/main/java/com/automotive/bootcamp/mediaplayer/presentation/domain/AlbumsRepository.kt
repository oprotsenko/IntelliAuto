package com.automotive.bootcamp.mediaplayer.presentation.domain

import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum

interface AlbumsRepository {

    suspend fun getAlbums(): List<MediaAlbum>

    suspend fun getAlbum(albumId: String): MediaAlbum
}