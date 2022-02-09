package com.automotive.bootcamp.mediaplayer.presentation.extensions

import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper

fun Playlist.mapToPlaylistWrapper(): PlaylistWrapper =
    PlaylistWrapper(playlist = this, playlistName = this.name)