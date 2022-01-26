package com.automotive.bootcamp.mediaplayer.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Audio(
    val id: Long = 0,
    val cover: String?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String
) : Parcelable