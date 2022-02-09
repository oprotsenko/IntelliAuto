package com.automotive.bootcamp.mediaplayer.data.cache.room.extensions

import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem

fun PlaylistItem.mapToPlaylistEntity(): PlaylistEntity =
    PlaylistEntity(
        name = this.name,
        pid = this.id
    )