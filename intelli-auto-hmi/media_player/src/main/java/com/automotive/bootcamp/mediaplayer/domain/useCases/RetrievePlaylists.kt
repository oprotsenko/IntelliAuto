package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository

class RetrievePlaylists(private val playlistRepository: PlaylistRepository) {
    suspend fun retrievePlaylists() =
        playlistRepository.getAllPlaylists()
}