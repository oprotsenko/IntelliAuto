package com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity

data class PlaylistWithAudios(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "pid",
        entityColumn = "aid",
        associateBy = Junction(AudioPlaylistCrossRefEntity::class)
    )
    val audios:List<AudioEntity>
)