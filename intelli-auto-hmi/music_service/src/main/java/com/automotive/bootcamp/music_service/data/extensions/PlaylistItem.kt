package com.automotive.bootcamp.music_service.data.extensions

import com.automotive.bootcamp.music_service.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import com.automotive.bootcamp.music_service.service.models.Playlist

fun PlaylistItem.mapToPlaylist() = Playlist(
    id = this.id,
    name = this.name,
    list = this.list?.map {
        it.mapToAudio()
    })

fun PlaylistItem.mapToEntity(): PlaylistEntity =
    PlaylistEntity(
        name = this.name,
        pid = this.id
    )