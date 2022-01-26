package com.automotive.bootcamp.mediaplayer.data.extensions

import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.models.Audio

fun AudioItem.mapToAudio() : Audio =
    Audio(
        id = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        url = this.url
    )

fun AudioItem.mapToEntity() : AudioEntity =
    AudioEntity(
        aid = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        url = this.url
    )
