package com.automotive.bootcamp.mediaplayer.data.storage.room.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper

data class PlaylistWithAudios(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "pid",
        entityColumn = "aid",
        associateBy = Junction(AudioPlaylistCrossRef::class)
    )
    val audios:List<AudioEntity>
)

fun PlaylistWithAudios.mapToPlaylistWrapper() : PlaylistWrapper =
    PlaylistWrapper(
        playlistName = this.playlist.name,
        playlist = Playlist(
            id = "0",
            list =this.audios.map {
                it.mapToAudio()
            }
        )
    )
