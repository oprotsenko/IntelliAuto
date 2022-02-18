package com.automotive.bootcamp.music_service.data.models

import com.automotive.bootcamp.music_service.utils.generateKey

data class AudioItem(
    val id: Long,
    val cover: String?,
    val title: String?,
    val artist: String?,
    val url: String
)