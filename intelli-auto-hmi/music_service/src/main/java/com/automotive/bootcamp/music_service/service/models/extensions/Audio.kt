package com.automotive.bootcamp.music_service.service.models.extensions

import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.service.models.Audio

//fun Audio.wrapAudio(isFavourite: Boolean = false, isRecent: Boolean = true): AudioWrapper =
//    AudioWrapper(
//        audio = this,
//        isFavourite = isFavourite,
//        isRecent = isRecent
//    )

fun Audio.mapToAudioItem(): AudioItem =
    AudioItem(
        id = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        url = this.url
    )