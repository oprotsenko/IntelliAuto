package com.automotive.bootcamp.mediaplayer.data.models

import android.graphics.Bitmap

data class AudioItem(
    val id: Long,
    val cover: Bitmap,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String
)