package com.automotive.bootcamp.mediaplayer.presentation.data

import com.automotive.bootcamp.mediaplayer.presentation.data.storage.LocalData
import com.automotive.bootcamp.mediaplayer.presentation.domain.AlbumsRepository

class MediaRepository(private val localData: LocalData): AlbumsRepository {

    override suspend fun getAlbums(): List<MediaAlbum> {
       return localData.getAlbums()
    }

    override suspend fun getAlbum(albumId: String): MediaAlbum {
        TODO("Not yet implemented")
    }
}