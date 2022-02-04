package com.automotive.bootcamp.music_service.service.models.extensions

import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.music_service.service.models.EmbeddedPlaylist

fun EmbeddedPlaylist.mapToSpecialPlaylistItem(): EmbeddedPlaylistItem =
    EmbeddedPlaylistItem(
        id = this.id,
        name = this.name,
    )