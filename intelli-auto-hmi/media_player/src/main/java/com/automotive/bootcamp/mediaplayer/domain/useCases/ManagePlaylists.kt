package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.domain.PlaylistMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ManagePlaylists(private val playlistRepository: PlaylistMediaRepository) {
    suspend fun createPlaylist(playlistName: String): Long =
        playlistRepository.addPlaylist(Playlist(name = playlistName, list = null))

    suspend fun addToPlaylist(aid: Long, pid: Long) = playlistRepository.addAudioToPlaylist(
        AudioPlaylistItemCrossRef(aid, pid)
    )

    suspend fun removeFromPlaylist(aid: Long, pid: Long) =
        playlistRepository.removeAudioFromPlaylist(
            AudioPlaylistItemCrossRef(aid, pid)
        )

    fun getAllPlaylists(): Flow<List<PlaylistWrapper>?> {
        return playlistRepository.getAllPlaylists().map { playlists ->
            playlists?.map {
                it.mapToPlaylistWrapper()
            }
        }
    }

    fun getPlaylist(pid: Long): Flow<PlaylistWrapper?> {
        return playlistRepository.getPlaylist(pid).map { playlist ->
            playlist?.mapToPlaylistWrapper()
        }
    }

    fun getPlaylistAudio(pid: Long): Flow<List<AudioWrapper>?>{
        return playlistRepository.getPlaylist(pid).map { playlist ->
            playlist?.list?.map {
                it.wrapAudio()
            }
        }
    }
}