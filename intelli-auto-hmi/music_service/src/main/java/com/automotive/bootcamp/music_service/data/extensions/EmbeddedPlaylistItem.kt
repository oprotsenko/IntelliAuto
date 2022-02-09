package com.automotive.bootcamp.music_service.data.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.EmbeddedPlaylistEntity
import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.music_service.service.models.EmbeddedPlaylist

fun EmbeddedPlaylistItem.mapToEmbeddedPlaylist() = EmbeddedPlaylist(
    id = this.id,
    name = this.name
)

fun EmbeddedPlaylistItem.mapToEntity(): EmbeddedPlaylistEntity =
    EmbeddedPlaylistEntity(
        pid = this.id,
        name = this.name
    )