package com.automotive.bootcamp.mediaplayer.data.cache.sources

import com.automotive.bootcamp.mediaplayer.data.local.model.SongItem

interface CacheAudioSource {
    suspend fun getAudio():List<SongItem>



}