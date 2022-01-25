package com.automotive.bootcamp.mediaplayer.domain.extensions

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

fun Audio.wrapAudio() : AudioWrapper =
    AudioWrapper(audio = this)

fun Audio.mapToAudioItem() : AudioItem =
    AudioItem(
        id = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        url = this.url
    )