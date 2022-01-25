package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem

class PlaylistRepository(private val cacheAudioSource: CacheAudioSource) {
    suspend fun addPlaylist(playlist: PlaylistItem): Long {
        return cacheAudioSource.insertPlaylist(playlist)
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

    suspend fun getPlaylistById(pid: Long): PlaylistItem? {
        return cacheAudioSource.getPlaylist(pid)
    }

    suspend fun getAllPlaylists(): List<PlaylistItem>? {
        return cacheAudioSource.getAllPlaylists()
    }
}