package com.automotive.bootcamp.mediaplayer.domain.models

import android.graphics.Bitmap

data class Audio(
    val id: Long,
    val cover: Bitmap?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val songURL: String
)