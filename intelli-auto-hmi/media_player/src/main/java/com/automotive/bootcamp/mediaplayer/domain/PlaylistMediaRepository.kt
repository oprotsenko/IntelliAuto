package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistMediaRepository {
    suspend fun playlistExists(pid: Long): Boolean
    suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean
    suspend fun addPlaylist(playlist: Playlist): Long
    suspend fun removePlaylist(pid: Long)
    suspend fun addAudioToPlaylist(crossRef: AudioPlaylistItemCrossRef)
    suspend fun removeAudioFromPlaylist(crossRef: AudioPlaylistItemCrossRef)
    fun getPlaylist(pid: Long): Flow<Playlist?>
    fun getAllPlaylists(): Flow<List<Playlist>?>
}