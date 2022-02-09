package com.automotive.bootcamp.mediaplayer.presentation.extensions

import android.support.v4.media.MediaBrowserCompat
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

fun MediaBrowserCompat.MediaItem.mapToAudioWrapper(
    isFavourite: Boolean = false,
    isRecent: Boolean = true
): AudioWrapper =
    AudioWrapper(
        audio = Audio(
            id = description.mediaId?.toLong() ?: 0,
            cover = description.iconUri.toString(),
            title = description.title.toString(),
            artist = description.subtitle.toString(),
            url = description.mediaUri.toString(),
        ),
        isFavourite = isFavourite,
        isRecent = isRecent
    )