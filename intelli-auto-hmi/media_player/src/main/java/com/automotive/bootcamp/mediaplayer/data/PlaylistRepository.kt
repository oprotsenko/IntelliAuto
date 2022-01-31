package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    fun getPlaylist(pid: Long): Flow<Playlist?> {
        return cacheAudioSource.getPlaylist(pid).map {
            it?.mapToPlaylist()
        }
    }

    fun getAllPlaylists(): Flow<List<Playlist>?> {
        return cacheAudioSource.getAllPlaylists().map { playlists ->
            playlists?.map { playlist ->
                playlist.mapToPlaylist()
            }
        }
    }
}