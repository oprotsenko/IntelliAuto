package com.automotive.bootcamp.mediaplayer.domain.extensions

import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper

fun Playlist.mapToPlaylistWrapper(): PlaylistWrapper =
    PlaylistWrapper(playlist = this, playlistName = this.name)

fun Playlist.mapToPlaylistItem(): PlaylistItem =
    PlaylistItem(
        id = this.id,
        name = this.name,
        list = this.list?.map {
            it.mapToAudioItem()
        }
    )