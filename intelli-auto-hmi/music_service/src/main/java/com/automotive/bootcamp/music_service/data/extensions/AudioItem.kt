package com.automotive.bootcamp.music_service.data.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.service.models.Audio

fun AudioItem.mapToAudio() : Audio =
    Audio(
        id = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        url = this.url
    )

fun AudioItem.mapToEntity() : AudioEntity =
    AudioEntity(
        aid = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        url = this.url
    )
