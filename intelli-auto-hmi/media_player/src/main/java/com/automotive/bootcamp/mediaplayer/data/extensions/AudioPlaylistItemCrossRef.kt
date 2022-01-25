package com.automotive.bootcamp.mediaplayer.data.extensions

import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef

fun AudioPlaylistItemCrossRef.mapToEntity() : AudioPlaylistCrossRefEntity =
    AudioPlaylistCrossRefEntity(
        aid = aid,
        pid = pid
    )