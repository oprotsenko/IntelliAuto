package com.automotive.bootcamp.mediaplayer.data.models

data class PlaylistItem(
    val id: Long = 0,
    val name: String,
    val list: List<AudioItem>?
)