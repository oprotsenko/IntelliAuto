package com.automotive.bootcamp.mediaplayer.data.models

data class PlaylistItem(
    val id: Long,
    val name: String,
    val list: List<AudioItem>?
)