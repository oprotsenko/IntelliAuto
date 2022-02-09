package com.automotive.bootcamp.music_service.service.models.extensions

import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import com.automotive.bootcamp.music_service.service.models.Playlist

//fun Playlist.mapToPlaylistWrapper(): PlaylistWrapper =
//    PlaylistWrapper(playlist = this, playlistName = this.name)

fun Playlist.mapToPlaylistItem(): PlaylistItem =
    PlaylistItem(
        id = this.id,
        name = this.name,
        list = this.list?.map {
            it.mapToAudioItem()
        }
    )