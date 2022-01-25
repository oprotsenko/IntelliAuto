package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AddRemoveFavourite(
    private val favouriteAudioRepository: FavouriteAudioRepository,
    private val playlistRepository: PlaylistRepository
) {
    fun addRemoveFavourite(audio: List<AudioWrapper>?, position: Int): List<AudioWrapper>? {
        val list = audio?.toMutableList()
        if (list?.get(position)?.isFavourite == true) {
            list[position] = list[position].copy(isFavourite = false)
        } else {
            list?.set(position, list[position].copy(isFavourite = true))
        }
        return list
    }

    suspend fun add(audios: List<AudioWrapper>?, position: Int) {
        val list = audios?.toMutableList()
        val audio = list?.get(position)?.unwrap()

        val favouritePlaylist = favouriteAudioRepository.getEmbeddedPlaylist()
        if (favouritePlaylist == null) {
            createPlaylist()
        }

        if (audio != null) {
            favouriteAudioRepository.addAudio(audio.id)
        }
    }

    private suspend fun createPlaylist() {
        val favouritePlaylist = Playlist(name = FAVOURITE_PLAYLIST_NAME, list = null)
        val favouritePlaylistId = playlistRepository.addPlaylist(favouritePlaylist)
        favouriteAudioRepository.addEmbeddedPlaylist(favouritePlaylistId)
    }
}