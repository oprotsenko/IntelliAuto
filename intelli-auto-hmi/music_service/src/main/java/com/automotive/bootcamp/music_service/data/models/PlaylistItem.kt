package com.automotive.bootcamp.music_service.data.models

import com.automotive.bootcamp.music_service.utils.generateKey

data class PlaylistItem(
    val id: Long = generateKey(),
    val name: String,
    val list: List<AudioItem>?
)