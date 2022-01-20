package com.automotive.bootcamp.mediaplayer.utils

import android.media.MediaPlayer
import android.util.Log
import com.automotive.bootcamp.mediaplayer.presentation.SongCompletionListener
import android.os.Handler
import android.os.Looper
import com.automotive.bootcamp.mediaplayer.extensions.currentSeconds
import com.automotive.bootcamp.mediaplayer.extensions.seconds
import com.automotive.bootcamp.mediaplayer.presentation.SongRunningListener

const val TAG = "DefaultMusicPlayer"

class DefaultMusicPlayer: MusicPlayer {

    private val player: MediaPlayer by lazy { MediaPlayer() }
    private lateinit var runnable:Runnable
    private var length = 0

    private lateinit var songCompletionListener: SongCompletionListener
    private lateinit var songRunningListener: SongRunningListener

    init {
        player.setOnCompletionListener{
            Log.d(TAG,"Song completed!")

            songCompletionListener.onSongCompletion()
        }

        initializeSeekBar()
    }

    override fun setOnSongCompletionListener(songCompletionListener: SongCompletionListener) {
        this.songCompletionListener = songCompletionListener
    }

    override fun setOnSongRunningListener(songRunningListener: SongRunningListener) {
        this.songRunningListener = songRunningListener
    }

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

    private fun initializeSeekBar() {
        runnable = Runnable {
            songRunningListener.onSongRunning(player.seconds, player.currentSeconds)
            Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
        }

        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
    }
}