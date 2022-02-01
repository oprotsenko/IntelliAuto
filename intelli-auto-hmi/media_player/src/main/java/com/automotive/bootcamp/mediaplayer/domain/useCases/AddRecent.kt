package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.util.Log
import com.automotive.bootcamp.mediaplayer.domain.PlaylistMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_CAPACITY
import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_NAME

class AddRecent(
    private val recentAudioRepository: RecentMediaRepository,
    private val playlistRepository: PlaylistMediaRepository,
) {
    suspend fun execute(aid: Long, recentAudios: List<Audio>?) {
        Log.d("AddRecent", "execute")

        tryCreatePlaylist()
        if (recentAudioRepository.hasAudio(aid)) {
            recentAudioRepository.removeAudio(aid)
        }
        checkPlaylistCapacity(recentAudios)
        recentAudioRepository.addAudio(aid)
    }

    private suspend fun checkPlaylistCapacity(recentAudios: List<Audio>?) {
        recentAudios?.let { audios ->
            if (audios.size >= RECENT_PLAYLIST_CAPACITY) {
                val aid = audios.minOf { it.id }
                recentAudioRepository.removeAudio(aid)
            }
        }
    }

    private suspend fun tryCreatePlaylist() {
        if (recentAudioRepository.getEmbeddedPlaylist() == null) {
            val recentPlaylist = Playlist(name = RECENT_PLAYLIST_NAME, list = null)
            val recentPlaylistId = playlistRepository.addPlaylist(recentPlaylist)
            recentAudioRepository.addEmbeddedPlaylist(recentPlaylistId)
        }
    }
}