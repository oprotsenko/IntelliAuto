package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist

class PlaylistRepository(private val cacheAudioSource: CacheAudioSource) {
    suspend fun playlistExists(pid:Long): Boolean {
        return cacheAudioSource.playlistExists(pid)
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

    suspend fun getAllPlaylists(): List<PlaylistItem>? {
        return cacheAudioSource.getAllPlaylists()
    }
}