package com.automotive.bootcamp.mediaplayer.presentation.models

import android.os.Parcelable
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistWrapper(
    val playlistName: String,
    val playlist: Playlist
) : Parcelable
