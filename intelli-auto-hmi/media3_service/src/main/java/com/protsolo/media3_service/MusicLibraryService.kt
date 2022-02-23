package com.protsolo.media3_service

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.protsolo.media3_service.sources.ServiceSources

class MusicLibraryService : MediaLibraryService() {

    private val sources = ServiceSources(this)
    private val tree by lazy { BrowseTree(sources) }
    private val player = ExoPlayer.Builder(this)
        .setAudioAttributes(
            AudioAttributes.DEFAULT, /* handleAudioFocus= */ true
        )
        .setHandleAudioBecomingNoisy(true)
        .setWakeMode(C.WAKE_MODE_LOCAL)
        .build()

    private val libraryCallback = object : MediaLibrarySession.MediaLibrarySessionCallback {

    }

    private val mediaLibrarySession = MediaLibrarySession.Builder(this, player, libraryCallback).build()

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaLibrarySession
    }
}