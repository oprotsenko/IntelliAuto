package com.automotive.bootcamp.music_service.data

import android.support.v4.media.MediaMetadataCompat

interface MusicSource : Iterable<MediaMetadataCompat> {
    fun whenReady(action: (Boolean) -> Unit): Boolean
    suspend fun load()
}