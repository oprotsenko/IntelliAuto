package com.automotive.bootcamp.music_service.data.cache.room.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.relations.PlaylistWithAudios
import com.automotive.bootcamp.music_service.data.models.PlaylistItem

fun PlaylistWithAudios.mapToPlaylistItem(): PlaylistItem =
    PlaylistItem(
        id = this.playlist.pid,
        name = this.playlist.name,
        list = this.audios.map {
            it.mapToAudioItem()
        }
    )