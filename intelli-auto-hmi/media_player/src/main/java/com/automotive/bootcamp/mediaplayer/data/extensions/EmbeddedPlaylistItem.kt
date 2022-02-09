package com.automotive.bootcamp.mediaplayer.data.extensions

import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist

fun EmbeddedPlaylistItem.mapToEmbeddedPlaylist() = EmbeddedPlaylist(
    id = this.id,
    name = this.name
)

fun EmbeddedPlaylist.mapToSpecialPlaylistItem(): EmbeddedPlaylistItem =
    EmbeddedPlaylistItem(
        id = this.id,
        name = this.name,
    )