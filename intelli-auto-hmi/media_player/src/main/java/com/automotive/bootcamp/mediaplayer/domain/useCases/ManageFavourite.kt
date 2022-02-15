package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.FavouriteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.PlaylistMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.utils.FAVOURITE_PLAYLIST_NAME

class ManageFavourite(
    private val favouriteAudioRepository: FavouriteMediaRepository
) {
    suspend fun removeFavourite(aid: Long) {
        favouriteAudioRepository.removeAudio(aid)
    }

    suspend fun addFavourite(aid: Long) {
        favouriteAudioRepository.addAudio(aid)
    }

    suspend fun hasAudio(aid: Long): Boolean =
        favouriteAudioRepository.hasAudio(aid)

    suspend fun getId(): Long? =
        favouriteAudioRepository.getEmbeddedPlaylist()?.id
}