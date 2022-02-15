package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface RecentMediaRepository {
    suspend fun addAudio(aid: Long)
    suspend fun removeAudio(aid: Long)
    suspend fun hasAudio(aid: Long): Boolean
    fun getPlaylist(): Flow<Playlist?>
    suspend fun getSize(): Int?
    suspend fun getEmbeddedPlaylist(): EmbeddedPlaylist?
    suspend fun addEmbeddedPlaylist(pid: Long)
}