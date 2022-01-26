package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist

class AddRemoveFavourite(
    private val favouriteAudioRepository: FavouriteAudioRepository,
    private val playlistRepository: PlaylistRepository
) {
    suspend fun addRemoveFavourite(aid: Long) : List<Long>? {
        checkFavouritePlaylist()
        if (favouriteAudioRepository.isAudioExist(aid)) {
            favouriteAudioRepository.removeAudio(aid)
        } else {
            favouriteAudioRepository.addAudio(aid)
        }
        return favouriteAudioRepository.getPlaylist()?.mapToPlaylist()?.list?.map { audio ->
            audio.id
        }
    }

    private suspend fun checkFavouritePlaylist() {
        val favouritePlaylist = favouriteAudioRepository.getEmbeddedPlaylist()
        if (favouritePlaylist == null) {
            createPlaylist()
        }
    }

    private suspend fun createPlaylist() {
        val favouritePlaylist = Playlist(name = FAVOURITE_PLAYLIST_NAME, list = null)
        val favouritePlaylistId = playlistRepository.addPlaylist(favouritePlaylist)
        favouriteAudioRepository.addEmbeddedPlaylist(favouritePlaylistId)
    }
}