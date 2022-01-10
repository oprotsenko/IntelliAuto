package com.automotive.bootcamp.mediaplayer.presentation.data

import androidx.lifecycle.LiveData

interface AlbumsRepository {

    suspend fun getAlbums(): LiveData<List<MediaAlbum>>

    suspend fun getAlbum(albumId: String): MediaAlbum
}