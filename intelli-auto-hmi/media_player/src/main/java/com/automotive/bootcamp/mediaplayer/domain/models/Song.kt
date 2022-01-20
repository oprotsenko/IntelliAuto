package com.automotive.bootcamp.mediaplayer.domain.models

import android.graphics.Bitmap

data class Song(
    val id: Long,
    val cover: Bitmap?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val songURL: String
)

data class SongWrapper(
    val song: Song,
    var isFavourite: Boolean = false,
    var isRecent: Boolean = false
)