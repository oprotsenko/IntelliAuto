package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem

class CreatePlaylist(private val playlistRepository: PlaylistRepository) {
    suspend fun createPlaylist(playlistName: String) {
        playlistRepository.addPlaylist(PlaylistItem(name = playlistName, list = null))
    }
}