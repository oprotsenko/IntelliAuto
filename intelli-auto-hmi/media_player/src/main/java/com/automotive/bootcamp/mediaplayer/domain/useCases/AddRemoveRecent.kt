package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.common.utils.RECENT_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.RecentAudioRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AddRemoveRecent(
    private val recentAudioRepository: RecentAudioRepository,
    private val playlistRepository: PlaylistRepository
) {
    suspend fun addRemoveRecent(audio: List<AudioWrapper>?, position: Int): List<AudioWrapper>? {
        val list = audio?.toMutableList()
        if (list?.get(position)?.isRecent == true) {
            recentAudioRepository.removeAudio(list[position].audio.id)
//            list[position] = list[position].copy(isRecent = false)
        } else {
            list?.let { recentAudioRepository.addAudio(list[position].audio.id) }
//            list?.set(position, list[position].copy(isRecent = true))
        }
        return recentAudioRepository.getPlaylist()?.mapToPlaylist()?.list?.map {
            it.wrapAudio()
        }
    }

    suspend fun add(audios: List<AudioWrapper>?, position: Int) {
        val list = audios?.toMutableList()
        val audio = list?.get(position)?.unwrap()

        val recentPlaylist = recentAudioRepository.getEmbeddedPlaylist()
        if (recentPlaylist == null) {
            createPlaylist()
        }

        if (audio != null) {
            recentAudioRepository.addAudio(audio.id)
        }
    }

    private suspend fun createPlaylist() {
        val recentPlaylist = Playlist(name = RECENT_PLAYLIST_NAME, list = null)
        val recentPlaylistId = playlistRepository.addPlaylist(recentPlaylist)
        recentAudioRepository.addEmbeddedPlaylist(recentPlaylistId)
    }
}