package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapPlaylist

class RetrievePlaylists(private val playlistRepository: PlaylistRepository) {
    suspend fun retrievePlaylists() =
        playlistRepository.getAllPlaylists()?.map { it.mapToPlaylist().wrapPlaylist() }
}