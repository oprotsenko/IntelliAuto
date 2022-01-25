package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository

class DeletePlaylist(private val playlistRepository: PlaylistRepository) {
    suspend fun deletePlaylist(id: Long) = playlistRepository.removePlaylist(id)
}