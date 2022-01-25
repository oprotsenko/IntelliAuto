package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef

class AddToPlaylist(private val playlistRepository: PlaylistRepository) {

    suspend fun addToPlaylist(aid: Long, pid: Long) = playlistRepository.addAudioToPlaylist(
        AudioPlaylistItemCrossRef(aid, pid)
    )
}