package com.automotive.bootcamp.mediaplayer.domain.models

import android.os.Parcelable
import com.automotive.bootcamp.mediaplayer.utils.extensions.generateKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long = generateKey(),//todo maybe keyGenerator is better?
    val name: String,
    val list: List<Audio>?
) : Parcelable