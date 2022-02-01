package com.automotive.bootcamp.mediaplayer.data.cache

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import kotlinx.coroutines.flow.Flow

interface CacheAudioSource {
    suspend fun insertAudio(audio: AudioItem): Long
    suspend fun insertAudios(audios: List<AudioItem>): List<Long>
    suspend fun playlistExists(pid: Long): Boolean
    suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean
    suspend fun insertEmbeddedPlaylist(playlist: EmbeddedPlaylistItem)
    suspend fun insertPlaylist(playlist: PlaylistItem): Long
    suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistItemCrossRef)
    suspend fun deletePlaylist(pid: Long)
    suspend fun deleteAudioFromPlaylist(crossRef: AudioPlaylistItemCrossRef)
    suspend fun getEmbeddedPlaylist(name: String): EmbeddedPlaylistItem?
    suspend fun getPlaylistSize(pid: Long): Int
    fun getPlaylist(pid: Long): Flow<PlaylistItem?>
    fun getAllPlaylists(): Flow<List<PlaylistItem>?>
}