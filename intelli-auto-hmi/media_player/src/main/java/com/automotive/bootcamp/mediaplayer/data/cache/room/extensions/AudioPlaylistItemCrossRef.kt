package com.automotive.bootcamp.mediaplayer.data.cache.room.extensions

import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef

fun AudioPlaylistItemCrossRef.mapToAudioPlaylistCrossRefEntity() : AudioPlaylistCrossRefEntity =
    AudioPlaylistCrossRefEntity(
        aid = aid,
        pid = pid
    )