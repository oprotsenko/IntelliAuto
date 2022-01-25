package com.automotive.bootcamp.mediaplayer.domain.extensions

import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.domain.models.AudioPlaylistCrossRef

fun AudioPlaylistCrossRef.mapToAudioPlaylistItemCrossRef(): AudioPlaylistItemCrossRef =
    AudioPlaylistItemCrossRef(
        aid = this.aid,
        pid = this.pid,
    )