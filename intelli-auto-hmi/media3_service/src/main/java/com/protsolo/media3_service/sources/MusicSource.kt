package com.protsolo.media3_service.sources

import androidx.media3.common.MediaMetadata

interface MusicSource : Iterable<MediaMetadata> {
    fun whenReady(action: (Boolean) -> Unit): Boolean
    suspend fun load()
}