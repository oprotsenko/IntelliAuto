package com.automotive.bootcamp.mediaplayer.data.storage.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audios")
data class AudioEntity(
    @PrimaryKey(autoGenerate = true)
    val aid: Int = 0,
    //  val cover: Bitmap,
    val title: String,
    val artist: String,
    val duration: String,
    val songURL: String,
)