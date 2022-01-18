package com.automotive.bootcamp.mediaplayer

import android.media.MediaPlayer
import com.automotive.bootcamp.mediaplayer.domain.models.Song

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

    override fun shufflePlaylist(songs: ArrayList<Song>) {
        TODO("Not yet implemented")
    }

    override fun repeatOne() {
        TODO("Not yet implemented")
    }
}