package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.PlaylistMediaRepository

class DeletePlaylist(private val playlistRepository: PlaylistMediaRepository) {
    suspend fun deletePlaylist(id: Long) = playlistRepository.removePlaylist(id)
}