package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.FavouriteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.PlaylistMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_NAME

class InitializeMediaPlayer(
    private val recentAudioRepository: RecentMediaRepository,
    private val favouriteAudioRepository: FavouriteMediaRepository,
    private val playlistRepository: PlaylistMediaRepository
) {
    suspend fun execute() {
        tryCreateRecentPlaylist()
        tryCreateFavouritePlaylist()
    }

    private suspend fun tryCreateRecentPlaylist() {
        if (recentAudioRepository.getEmbeddedPlaylist() == null) {
            val recentPlaylist = Playlist(name = RECENT_PLAYLIST_NAME, list = null)
            val recentPlaylistId = playlistRepository.addPlaylist(recentPlaylist)
            recentAudioRepository.addEmbeddedPlaylist(recentPlaylistId)
        }
    }

    private suspend fun tryCreateFavouritePlaylist() {
        if (favouriteAudioRepository.getEmbeddedPlaylist() == null) {
            val favouritePlaylist = Playlist(name = FAVOURITE_PLAYLIST_NAME, list = null)
            val favouritePlaylistId = playlistRepository.addPlaylist(favouritePlaylist)
            favouriteAudioRepository.addEmbeddedPlaylist(favouritePlaylistId)
        }
    }
}