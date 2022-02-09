package com.automotive.bootcamp.music_service.data.cache.room.entities.relations

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.automotive.bootcamp.music_service.data.cache.room.entities.PlaylistEntity

@Entity(
    tableName = "audio_playlist_cross_ref_SERV",
    primaryKeys = ["aid","pid"],
    foreignKeys = [
        ForeignKey(entity = PlaylistEntity::class,
            parentColumns = ["pid"],
            childColumns = ["pid"],
            onDelete = CASCADE)])

data class AudioPlaylistCrossRefEntity(
    val aid:Long,
    val pid:Long
)