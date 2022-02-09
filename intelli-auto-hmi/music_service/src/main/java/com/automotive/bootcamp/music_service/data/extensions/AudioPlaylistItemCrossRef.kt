package com.automotive.bootcamp.music_service.data.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef

fun AudioPlaylistItemCrossRef.mapToEntity() : AudioPlaylistCrossRefEntity =
    AudioPlaylistCrossRefEntity(
        aid = aid,
        pid = pid
    )