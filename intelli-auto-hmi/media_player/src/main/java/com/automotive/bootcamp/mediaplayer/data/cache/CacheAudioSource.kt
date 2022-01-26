package com.automotive.bootcamp.mediaplayer.data.cache

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist

interface CacheAudioSource {
    suspend fun insertAudio(audio: AudioItem): Long
    suspend fun insertAudios(audios: List<AudioItem>): List<Long>
    suspend fun playlistExists(pid: Long): Boolean
    suspend fun playlistHasAudio(pid:Long, aid:Long): Boolean
    suspend fun insertEmbeddedPlaylist(playlist: EmbeddedPlaylistItem)
    suspend fun getEmbeddedPlaylist(name: String): EmbeddedPlaylistItem?
    suspend fun insertPlaylist(playlist: PlaylistItem): Long
    suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistItemCrossRef)
    suspend fun deletePlaylist(pid: Long)
    suspend fun deleteAudioFromPlaylist(crossRef: AudioPlaylistItemCrossRef)
    suspend fun getPlaylist(pid: Long): PlaylistItem?
    suspend fun getAllPlaylists(): List<PlaylistItem>?
    suspend fun isAudioExists(aid: Long, pid: Long): Boolean
}