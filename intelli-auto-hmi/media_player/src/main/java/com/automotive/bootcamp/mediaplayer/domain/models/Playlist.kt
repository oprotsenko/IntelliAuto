package com.automotive.bootcamp.mediaplayer.domain.models

import android.os.Parcelable
import com.automotive.bootcamp.common.utils.generateKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long = generateKey(),
    val name: String,
    val list: List<Audio>?
) : Parcelable