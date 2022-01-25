package com.automotive.bootcamp.mediaplayer.data.storage.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.automotive.bootcamp.mediaplayer.domain.models.Audio

@Entity(tableName = "audios")
data class AudioEntity(
    @PrimaryKey(autoGenerate = true)
    val aid: Int = 0,
    //  val cover: Bitmap,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String,
)

fun AudioEntity.mapToAudio() : Audio =
    Audio(
        id = this.aid.toLong(),
        cover = null,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        url = this.url
    )