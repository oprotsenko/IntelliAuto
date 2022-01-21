package com.automotive.bootcamp.mediaplayer.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val pid: Int = 0,
    val name: String,
    val type: String,
)