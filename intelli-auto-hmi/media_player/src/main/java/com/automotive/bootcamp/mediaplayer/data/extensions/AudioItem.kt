package com.automotive.bootcamp.mediaplayer.data.extensions

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.models.Audio

fun AudioItem.mapToAudio() : Audio =
    Audio(
        id = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        url = this.url
    )

fun Audio.mapToAudioItem(): AudioItem =
    AudioItem(
        id = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        url = this.url
    )
