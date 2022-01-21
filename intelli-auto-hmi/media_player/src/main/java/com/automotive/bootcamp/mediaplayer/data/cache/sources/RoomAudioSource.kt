package com.automotive.bootcamp.mediaplayer.data.cache.sources

import android.content.Context
import com.automotive.bootcamp.mediaplayer.data.cache.room.RoomAudioDatabase
import com.automotive.bootcamp.mediaplayer.data.local.model.SongItem

class RoomAudioSource (context: Context): CacheAudioSource {
    private val dao = RoomAudioDatabase.getInstance(context).audioDao

    override suspend fun getAudio(): List<SongItem> {
        val list = mutableListOf<SongItem>()

        return list
    }
}