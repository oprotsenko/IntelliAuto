package com.automotive.bootcamp.mediaplayer.data.models

data class AudioItem(
    val id: Long,
    val cover: String?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String
)