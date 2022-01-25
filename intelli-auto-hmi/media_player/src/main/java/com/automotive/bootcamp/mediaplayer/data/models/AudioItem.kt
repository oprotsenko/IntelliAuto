package com.automotive.bootcamp.mediaplayer.data.models

import android.graphics.Bitmap
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.AudioEntity
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
        url = this.url
    )

fun AudioItem.mapToEntity() : AudioEntity =
    AudioEntity(
        aid = this.id.toInt(),
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        url = this.url
    )
