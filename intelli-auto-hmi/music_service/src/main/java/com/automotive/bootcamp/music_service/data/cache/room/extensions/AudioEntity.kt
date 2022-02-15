package com.automotive.bootcamp.music_service.data.cache.room.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.music_service.data.models.AudioItem


fun AudioEntity.mapToAudioItem() : AudioItem =
    AudioItem(
        id = this.aid,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        url = this.url
    )

fun AudioItem.mapToAudioEntity() : AudioEntity =
    AudioEntity(
        aid = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        url = this.url
    )