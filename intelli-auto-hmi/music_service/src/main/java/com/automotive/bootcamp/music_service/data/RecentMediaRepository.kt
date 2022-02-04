package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import kotlinx.coroutines.flow.Flow

interface RecentMediaRepository {

    suspend fun addAudio(aid: Long)
    suspend fun removeAudio(aid: Long)
    suspend fun hasAudio(aid: Long): Boolean
    fun getPlaylist(): Flow<PlaylistItem?>?
    suspend fun getSize(): Int?
    suspend fun getEmbeddedPlaylist(): EmbeddedPlaylistItem?
    suspend fun addEmbeddedPlaylist(pid: Long)
}