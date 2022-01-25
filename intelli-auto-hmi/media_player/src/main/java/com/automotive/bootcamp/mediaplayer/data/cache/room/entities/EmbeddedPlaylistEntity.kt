package com.automotive.bootcamp.mediaplayer.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "embedded_playlists")
data class EmbeddedPlaylistEntity (
    @PrimaryKey
    val name: String,
    val pid: Long
)