package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper

class RetrievePlaylistAudio(private val playlistRepository: PlaylistRepository) {

    suspend fun retrievePlaylists() =
        playlistRepository.getAllPlaylists()

    suspend fun retrievePlaylist(pid: Long): PlaylistWrapper? {
        val list = playlistRepository.getPlaylist(pid)
        return if (list != null) {
            PlaylistWrapper(list.name, Playlist(list.id, list.name, list.list?.map {
                it.mapToAudio()
            }))
        } else null
    }
}