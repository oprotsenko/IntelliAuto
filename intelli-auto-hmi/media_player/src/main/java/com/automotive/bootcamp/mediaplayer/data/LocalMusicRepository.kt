package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.local.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.local.model.SongItem
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class LocalMusicRepository(private val localMedia: LocalMedia):
    LocalMediaRepository {

    override suspend fun getLocalSongs(): List<SongItem> {
       return localMedia.getLocalSongs()
    }
}