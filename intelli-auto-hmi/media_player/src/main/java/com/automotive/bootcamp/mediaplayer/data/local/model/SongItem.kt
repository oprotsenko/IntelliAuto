package com.automotive.bootcamp.mediaplayer.data.local.model

import android.graphics.Bitmap

data class SongItem(
    val id: Long,
    val cover: Bitmap?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val songURL: String
)
