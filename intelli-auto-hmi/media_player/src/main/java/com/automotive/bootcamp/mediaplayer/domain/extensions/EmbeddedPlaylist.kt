package com.automotive.bootcamp.mediaplayer.domain.extensions

import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist

fun EmbeddedPlaylist.mapToSpecialPlaylistItem(): EmbeddedPlaylistItem =
    EmbeddedPlaylistItem(
        id = this.id,
        name = this.name,
    )