package com.automotive.bootcamp.music_service.service.callbacks

import android.widget.Toast
import com.automotive.bootcamp.music_service.service.MusicService
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class MusicPlayerEventListener(
    private val musicService: MusicService
) : Player.Listener {
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "some error", Toast.LENGTH_SHORT).show()
    }
}