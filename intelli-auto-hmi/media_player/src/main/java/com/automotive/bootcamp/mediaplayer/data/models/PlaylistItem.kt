package com.automotive.bootcamp.mediaplayer.data.models

import com.automotive.bootcamp.mediaplayer.utils.extensions.generateKey

data class PlaylistItem(
    val id: Long = generateKey(),
    val name: String,
    val list: List<AudioItem>?
)