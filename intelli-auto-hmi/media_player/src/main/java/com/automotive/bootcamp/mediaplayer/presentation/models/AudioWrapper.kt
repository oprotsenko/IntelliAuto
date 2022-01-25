package com.automotive.bootcamp.mediaplayer.presentation.models

import com.automotive.bootcamp.mediaplayer.domain.models.Audio

data class AudioWrapper(
    val audio: Audio,
    var isFavourite: Boolean = false,
    var isRecent: Boolean = true
)