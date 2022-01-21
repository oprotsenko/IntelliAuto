package com.automotive.bootcamp.mediaplayer.data.cache

import com.automotive.bootcamp.mediaplayer.data.cache.sources.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.local.model.SongItem
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class CacheAudioRepository(private val cacheAudioSource: CacheAudioSource):
    LocalMediaRepository {

    override suspend fun getLocalSongs(): List<SongItem> {
        return cacheAudioSource.getAudio()
    }
}