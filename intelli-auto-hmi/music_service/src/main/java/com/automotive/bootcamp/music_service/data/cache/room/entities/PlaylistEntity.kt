package com.automotive.bootcamp.music_service.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_SERV")
data class PlaylistEntity(
    @PrimaryKey
    val pid: Long,
    val name: String,
)