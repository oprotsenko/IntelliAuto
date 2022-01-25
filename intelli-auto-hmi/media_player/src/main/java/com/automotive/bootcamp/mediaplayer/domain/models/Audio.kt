package com.automotive.bootcamp.mediaplayer.domain.models

import android.graphics.Bitmap
import android.os.Parcelable
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Audio(
    val id: Long,
    val cover: Bitmap?,
    val title: String?,
    val artist: String?,
    val duration: String?,
    val url: String
) : Parcelable

fun Audio.wrapAudio() : AudioWrapper =
    AudioWrapper(audio = this)