package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.EmbeddedPlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper

class PlaylistRepository(private val cacheAudioSource: CacheAudioSource) {
    suspend fun playlistExists(pid: Long): Boolean {
        return cacheAudioSource.playlistExists(pid)
    }

    suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean {
        return cacheAudioSource.playlistHasAudio(pid, aid)
    }

    suspend fun addPlaylist(playlist: Playlist): Long {
        return cacheAudioSource.insertPlaylist(playlist.mapToPlaylistItem())
    }

    suspend fun removePlaylist(pid: Long) {
        cacheAudioSource.deletePlaylist(pid)
    }

    suspend fun addAudioToPlaylist(crossRef: AudioPlaylistItemCrossRef) {
        cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
    }

    suspend fun removeAudioFromPlaylist(crossRef: AudioPlaylistItemCrossRef) {
        cacheAudioSource.deleteAudioFromPlaylist(crossRef)
    }

    suspend fun getPlaylist(pid: Long): PlaylistItem? {
        return cacheAudioSource.getPlaylist(pid)
    }

    suspend fun getAllPlaylists(): List<PlaylistWrapper>? {
        return cacheAudioSource.getAllPlaylists()?.map {
            it.mapToPlaylist().mapToPlaylistWrapper()
        }
    }

    suspend fun getEmbeddedPlaylist(name: String): EmbeddedPlaylistItem? =
        cacheAudioSource.getEmbeddedPlaylist(name)
}