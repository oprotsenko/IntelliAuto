package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.common.utils.RECENT_PLAYLIST_CAPACITY
import com.automotive.bootcamp.common.utils.RECENT_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AddRecent(
    private val recentAudioRepository: RecentAudioRepository,
    private val playlistRepository: PlaylistRepository,
) {
    suspend fun execute(audioWrapped: AudioWrapper) {
        tryCreatePlaylist()

        val aid = audioWrapped.audio.id

        if (recentAudioRepository.hasAudio(aid)) {
            recentAudioRepository.removeAudio(aid)
        }

        checkPlaylistCapacity()

        recentAudioRepository.addAudio(aid)
    }

    private suspend fun checkPlaylistCapacity(){
        val recentPlaylistAudios = recentAudioRepository.getPlaylist()?.list

        recentPlaylistAudios?.let { audios ->
            if (audios.size >= RECENT_PLAYLIST_CAPACITY) {
                val aid = audios.minOf { it.id }
                recentAudioRepository.removeAudio(aid)
            }
        }
    }

    private suspend fun tryCreatePlaylist() {
        val embeddedRecentPlaylist = recentAudioRepository.getEmbeddedPlaylist()

        if (embeddedRecentPlaylist == null) {
            val recentPlaylist = Playlist(name = RECENT_PLAYLIST_NAME, list = null)
            val recentPlaylistId = playlistRepository.addPlaylist(recentPlaylist)
            recentAudioRepository.addEmbeddedPlaylist(recentPlaylistId)
        }
    }
}