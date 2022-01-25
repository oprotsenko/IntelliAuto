package com.automotive.bootcamp.mediaplayer.presentation.extensions

import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

fun AudioWrapper.unwrap() : Audio =
    this.audio