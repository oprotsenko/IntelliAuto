package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.storage.LocalMedia
import com.automotive.bootcamp.mediaplayer.domain.MediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Song

class MusicRepository(private val localMedia: LocalMedia):
    MediaRepository {

    override suspend fun getAlbums(): List<Song> {
       return localMedia.getAlbums()
    }

    override suspend fun getAlbum(albumId: String): Song {
        TODO("Not yet implemented")
    }
}