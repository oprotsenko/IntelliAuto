package com.automotive.bootcamp.mediaplayer.presentation.extensions

import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

fun AudioWrapper.unwrap() : Audio =
    this.audio

fun Audio.wrapAudio(isFavourite: Boolean = false, isRecent: Boolean = true): AudioWrapper =
    AudioWrapper(
        audio = this,
        isFavourite = isFavourite,
        isRecent = isRecent
    )