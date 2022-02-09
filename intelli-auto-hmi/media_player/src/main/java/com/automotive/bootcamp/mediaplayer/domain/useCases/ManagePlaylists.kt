package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.PlaylistMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.domain.models.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ManagePlaylists(private val playlistRepository: PlaylistMediaRepository) {
    suspend fun createPlaylist(playlistName: String): Long =
        playlistRepository.addPlaylist(Playlist(name = playlistName, list = null))

    suspend fun addToPlaylist(aid: Long, pid: Long) =
        playlistRepository.addAudioToPlaylist(AudioPlaylistCrossRef(aid, pid))

    suspend fun removeFromPlaylist(aid: Long, pid: Long) =
        playlistRepository.removeAudioFromPlaylist(AudioPlaylistCrossRef(aid, pid))

    fun getAllPlaylists(): Flow<List<Playlist>?> {
        return playlistRepository.getAllPlaylists()
    }

    fun getPlaylist(pid: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylist(pid)
    }

    fun getPlaylistAudio(pid: Long): Flow<List<Audio>?>{
        return playlistRepository.getPlaylist(pid).map { playlist ->
            playlist?.list
        }
    }
}