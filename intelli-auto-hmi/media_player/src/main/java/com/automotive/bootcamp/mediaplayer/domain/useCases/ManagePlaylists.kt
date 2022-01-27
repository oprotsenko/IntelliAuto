package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class ManagePlaylists(private val playlistRepository: PlaylistRepository) {

    suspend fun createPlaylist(playlistName: String): Long =
        playlistRepository.addPlaylist(Playlist(name = playlistName, list = null))

    suspend fun addToPlaylist(aid: Long, pid: Long) = playlistRepository.addAudioToPlaylist(
        AudioPlaylistItemCrossRef(aid, pid)
    )

    suspend fun getEmbeddedPlaylist(name: String) = playlistRepository.getEmbeddedPlaylist(name)

    suspend fun getAllPlaylists() = playlistRepository.getAllPlaylists()

    suspend fun removeFromPlaylist(aid: Long, pid: Long) =
        playlistRepository.removeAudioFromPlaylist(
            AudioPlaylistItemCrossRef(aid, pid)
        )

    suspend fun getPlaylist(pid: Long): List<AudioWrapper>? {
        val playlist = playlistRepository.getPlaylist(pid)
        return playlist?.list?.map {
            it.mapToAudio()
        }?.map {
            it.wrapAudio()
        }
    }
}