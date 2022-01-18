package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.local.LocalMedia
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Song

class LocalMusicRepository(private val localMedia: LocalMedia):
    LocalMediaRepository {

    override suspend fun getLocalSongs(): List<Song> {
       return localMedia.getLocalSongs()
    }
}