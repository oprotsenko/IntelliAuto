package com.automotive.bootcamp.mediaplayer.data.cache.room.extensions

import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

fun AudioEntity.mapToAudioItem() : AudioItem =
    AudioItem(
        id = this.aid,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        url = this.url
    )