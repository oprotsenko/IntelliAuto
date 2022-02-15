package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface FavouriteMediaRepository {
    suspend fun addAudio(aid: Long)
    suspend fun removeAudio(aid: Long)
    suspend fun hasAudio(aid: Long): Boolean
    fun getPlaylist(): Flow<Playlist?>
    suspend fun getEmbeddedPlaylist(): EmbeddedPlaylist?
    suspend fun addEmbeddedPlaylist(pid: Long)
}