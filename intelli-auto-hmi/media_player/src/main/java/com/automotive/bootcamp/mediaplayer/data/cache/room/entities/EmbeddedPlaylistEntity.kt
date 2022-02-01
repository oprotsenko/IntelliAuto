package com.automotive.bootcamp.mediaplayer.data.cache.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "embedded_playlists",
    foreignKeys = [
        ForeignKey(entity = PlaylistEntity::class,
            parentColumns = ["pid"],
            childColumns = ["pid"],
            onDelete = ForeignKey.CASCADE
        )])
data class EmbeddedPlaylistEntity (
    @PrimaryKey
    val name: String,
    val pid: Long
)