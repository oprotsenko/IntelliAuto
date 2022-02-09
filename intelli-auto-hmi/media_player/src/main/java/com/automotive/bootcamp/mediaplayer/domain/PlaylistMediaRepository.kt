package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistMediaRepository {
    suspend fun playlistExists(pid: Long): Boolean
    suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean
    suspend fun addPlaylist(playlist: Playlist): Long
    suspend fun removePlaylist(pid: Long)
    suspend fun addAudioToPlaylist(crossRef: AudioPlaylistCrossRef)
    suspend fun removeAudioFromPlaylist(crossRef: AudioPlaylistCrossRef)
    fun getPlaylist(pid: Long): Flow<Playlist?>
    fun getAllPlaylists(): Flow<List<Playlist>?>
}