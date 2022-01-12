package com.automotive.bootcamp.mediaplayer.presentation.data

interface AlbumsRepository {

    suspend fun getAlbums(): List<MediaAlbum>

    suspend fun getAlbum(albumId: String): MediaAlbum
}