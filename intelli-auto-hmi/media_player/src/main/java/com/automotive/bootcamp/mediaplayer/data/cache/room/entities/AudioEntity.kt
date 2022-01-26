package com.automotive.bootcamp.mediaplayer.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audios")
data class AudioEntity(
    @PrimaryKey
    val aid: Long,
    val cover: String?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String,
)