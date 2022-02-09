package com.automotive.bootcamp.mediaplayer.data.extensions

import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist

fun PlaylistItem.mapToPlaylist() = Playlist(
    id = this.id,
    name = this.name,
    list = this.list?.map {
        it.mapToAudio()
    })

fun Playlist.mapToPlaylistItem(): PlaylistItem =
    PlaylistItem(
        id = this.id,
        name = this.name,
        list = this.list?.map {
            it.mapToAudioItem()
        }
    )