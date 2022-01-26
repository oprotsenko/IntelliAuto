package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist

class AddRemoveFavourite(
    private val favouriteAudioRepository: FavouriteAudioRepository,
    private val playlistRepository: PlaylistRepository
) {
    suspend fun removeFavourite(aid: Long) {
        favouriteAudioRepository.removeAudio(aid)
    }


    suspend fun addFavourite(aid: Long) {
        setFavouritePlaylist()
        favouriteAudioRepository.addAudio(aid)
    }

    suspend fun hasAudio(aid: Long) =
        favouriteAudioRepository.hasAudio(aid)

    private suspend fun setFavouritePlaylist() {
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