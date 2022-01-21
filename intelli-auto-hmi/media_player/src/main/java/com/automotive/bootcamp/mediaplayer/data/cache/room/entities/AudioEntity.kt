package com.automotive.bootcamp.mediaplayer.data.cache.room.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audios")
data class AudioEntity(
    @PrimaryKey
    val aid: Int,
    val cover: Bitmap,
    val title: String,
    val artist: String,
    val duration: String,
    val songURL: String,
)
