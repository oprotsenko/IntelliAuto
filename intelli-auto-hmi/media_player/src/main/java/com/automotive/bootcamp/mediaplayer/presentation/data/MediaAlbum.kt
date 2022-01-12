package com.automotive.bootcamp.mediaplayer.presentation.data

data class MediaAlbum(
    val id: String,
    val artImage: ByteArray?,
    val songTitle: String?,
    val singerName: String?,
    val songDuration: String?,
)