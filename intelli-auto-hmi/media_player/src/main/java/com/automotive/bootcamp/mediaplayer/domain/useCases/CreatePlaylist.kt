package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist

class CreatePlaylist(private val playlistRepository: PlaylistRepository) {
    suspend fun createPlaylist(playlistName: String) {
        playlistRepository.addPlaylist(Playlist(name = playlistName, list = null))
    }
}