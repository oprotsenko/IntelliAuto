package com.automotive.bootcamp.mediaplayer.domain.models

import android.os.Parcelable
import com.automotive.bootcamp.common.utils.generateKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Audio(
    val id: Long = generateKey(),
    val cover: String?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String
) : Parcelable