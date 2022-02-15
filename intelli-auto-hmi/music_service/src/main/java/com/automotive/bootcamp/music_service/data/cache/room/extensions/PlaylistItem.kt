package com.automotive.bootcamp.music_service.data.cache.room.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.music_service.data.models.PlaylistItem

fun PlaylistItem.mapToPlaylistEntity(): PlaylistEntity =
    PlaylistEntity(
        name = this.name,
        pid = this.id
    )