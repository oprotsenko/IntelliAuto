package com.automotive.bootcamp.music_service.data.cache.room.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef

fun AudioPlaylistItemCrossRef.mapToAudioPlaylistCrossRefEntity() : AudioPlaylistCrossRefEntity =
    AudioPlaylistCrossRefEntity(
        aid = aid,
        pid = pid
    )