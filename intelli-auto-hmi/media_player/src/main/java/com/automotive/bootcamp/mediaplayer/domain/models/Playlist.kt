package com.automotive.bootcamp.mediaplayer.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long,
    val name: String,
    val list: List<Audio>?
) : Parcelable