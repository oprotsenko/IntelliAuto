package com.automotive.bootcamp.music_browse_service.sources

import android.support.v4.media.MediaMetadataCompat

interface MusicSource : Iterable<MediaMetadataCompat> {
    fun whenReady(action: (Boolean) -> Unit): Boolean
    fun load()
}