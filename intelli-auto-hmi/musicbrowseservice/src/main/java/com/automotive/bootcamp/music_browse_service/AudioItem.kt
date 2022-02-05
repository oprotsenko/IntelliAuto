package com.automotive.bootcamp.music_browse_service

data class AudioItem(
    val id: Long = 0,
    val cover: String?,
    val title: String?,
    val artist: String?,
    val url: String
)