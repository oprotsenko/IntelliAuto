package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.Song

interface MediaRepository {

    suspend fun getAlbums(): List<Song>

    suspend fun getAlbum(albumId: String): Song
}