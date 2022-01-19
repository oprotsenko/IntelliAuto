package com.automotive.bootcamp.mediaplayer.utils

import android.media.MediaPlayer

class DefaultMusicPlayer : MusicPlayer {
    private val player: MediaPlayer by lazy { MediaPlayer() }
    private var length = 0

    override fun play(songURL: String?) {
        player.apply {
            if (length > 0) {
                seekTo(length)
            } else {
                reset()
                setDataSource(songURL)
                prepare()
            }
            start()
        }
    }

    override fun pause() {
        player.apply {
            pause()
            length = currentPosition;
        }
    }

    override fun previousSong(songURL: String?) {
        length = 0
        play(songURL)
    }

    override fun nextSong(songURL: String?) {
        length = 0
        play(songURL)
    }
}