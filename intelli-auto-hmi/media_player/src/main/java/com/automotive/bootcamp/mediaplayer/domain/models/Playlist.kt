package com.automotive.bootcamp.mediaplayer.domain.models

import android.os.Parcelable
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: String,
    val list: List<Audio>
) : Parcelable

fun Playlist.wrapPlaylist(): PlaylistWrapper =
    PlaylistWrapper(
        playlistName = this.id,
        playlist = this
    )
