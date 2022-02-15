package com.automotive.bootcamp.music_service.data.cache.room.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.EmbeddedPlaylistEntity
import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem

fun EmbeddedPlaylistEntity.mapToEmbeddedPlaylistItem(): EmbeddedPlaylistItem =
    EmbeddedPlaylistItem(
        id = this.pid,
        name = this.name,
    )

fun EmbeddedPlaylistItem.mapToEmbeddedPlaylistEntity(): EmbeddedPlaylistEntity =
    EmbeddedPlaylistEntity(
        pid = this.id,
        name = this.name
    )