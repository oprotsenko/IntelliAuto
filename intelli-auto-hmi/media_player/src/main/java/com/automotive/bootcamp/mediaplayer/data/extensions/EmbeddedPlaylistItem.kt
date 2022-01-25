package com.automotive.bootcamp.mediaplayer.data.extensions

import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.EmbeddedPlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist

fun EmbeddedPlaylistItem.mapToEmbeddedPlaylist() = EmbeddedPlaylist(
    id = this.id,
    name = this.name
)

fun EmbeddedPlaylistItem.mapToEntity(): EmbeddedPlaylistEntity =
    EmbeddedPlaylistEntity(
        pid = this.id,
        name = this.name
    )