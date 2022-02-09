package com.automotive.bootcamp.music_service.service.models.extensions

import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.music_service.service.models.AudioPlaylistCrossRef

fun AudioPlaylistCrossRef.mapToAudioPlaylistItemCrossRef(): AudioPlaylistItemCrossRef =
    AudioPlaylistItemCrossRef(
        aid = this.aid,
        pid = this.pid,
    )