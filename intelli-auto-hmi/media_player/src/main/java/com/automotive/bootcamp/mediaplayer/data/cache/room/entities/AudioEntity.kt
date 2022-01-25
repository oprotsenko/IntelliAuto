package com.automotive.bootcamp.mediaplayer.data.cache.room.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

@Entity(tableName = "audios")
data class AudioEntity(
    @PrimaryKey(autoGenerate = true)
    val aid: Long = 0,
    val cover: Bitmap?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String,
)

fun AudioEntity.mapToAudioItem() : AudioItem =
    AudioItem(
        id = this.aid,
        cover = this.cover,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        url = this.url
    )