package com.automotive.bootcamp.mediaplayer.domain.models

data class Song(
    val id: Long,
    val cover: String?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val songURL: String
)