package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist

class ManageFavourite(
    private val favouriteAudioRepository: FavouriteAudioRepository,
    private val playlistRepository: PlaylistRepository
) {
    suspend fun removeFavourite(aid: Long) {
        favouriteAudioRepository.removeAudio(aid)
    }

    suspend fun addFavourite(aid: Long) {
        tryCreatePlaylist()
        favouriteAudioRepository.addAudio(aid)
    }

    suspend fun hasAudio(aid: Long): Boolean =
        favouriteAudioRepository.hasAudio(aid)

    suspend fun getId(): Long? =
        favouriteAudioRepository.getEmbeddedPlaylist()?.id

    private suspend fun tryCreatePlaylist() {
        if (favouriteAudioRepository.getEmbeddedPlaylist() == null) {
            val favouritePlaylist = Playlist(name = FAVOURITE_PLAYLIST_NAME, list = null)
            val favouritePlaylistId = playlistRepository.addPlaylist(favouritePlaylist)
            favouriteAudioRepository.addEmbeddedPlaylist(favouritePlaylistId)
        }
    }
}