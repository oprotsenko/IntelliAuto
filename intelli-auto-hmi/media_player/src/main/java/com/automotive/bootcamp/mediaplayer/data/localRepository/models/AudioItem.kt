package com.automotive.bootcamp.mediaplayer.data.localRepository.models

import android.graphics.Bitmap
import com.automotive.bootcamp.mediaplayer.domain.models.Audio

data class AudioItem(
    val id: Long,
    val cover: Bitmap?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String
)

fun AudioItem.mapToAudio() : Audio =
    Audio(
        id = this.id,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        songURL = this.url
    )
