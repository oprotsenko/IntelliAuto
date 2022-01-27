package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist

class ManagePlaylists(private val playlistRepository: PlaylistRepository) {

    suspend fun createPlaylist(playlistName: String) : Long =
        playlistRepository.addPlaylist(Playlist(name = playlistName, list = null))

    suspend fun addToPlaylist(aid: Long, pid: Long) = playlistRepository.addAudioToPlaylist(
        AudioPlaylistItemCrossRef(aid, pid)
    )

    suspend fun getEmbeddedPlaylist(name: String) = playlistRepository.getEmbeddedPlaylist(name)

    suspend fun getAllPlaylists() = playlistRepository.getAllPlaylists()
}