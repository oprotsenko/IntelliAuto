package com.automotive.bootcamp.mediaplayer.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import android.os.Handler
import android.os.Looper
import com.automotive.bootcamp.mediaplayer.utils.extensions.currentSeconds
import com.automotive.bootcamp.mediaplayer.utils.extensions.seconds
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener

class DefaultAudioPlayer(private val context: Context) : AudioPlayer {
    private val player: MediaPlayer by lazy { MediaPlayer() }
    private lateinit var audioCompletionListener: AudioCompletionListener
    private lateinit var audioRunningListener: AudioRunningListener
    private lateinit var runnable: Runnable

    private var length = 0

    init {
        player.setOnCompletionListener {
            length = 0
            audioCompletionListener.onAudioCompletion()
        }

        initializeRunnable()
    }

    private fun initializeRunnable() {
        runnable = Runnable {
            audioRunningListener.onAudioRunning(player.seconds, player.currentSeconds)
            Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
        }

        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
    }

    override fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener) {
        this.audioCompletionListener = audioCompletionListener
    }

    override fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener) {
        this.audioRunningListener = audioRunningListener
    }

    override fun playAudio(songURL: String?) {
        player.apply {
            if (length > 0) {
                seekTo(length)
            } else {
                reset()
                setDataSource(context, Uri.parse(songURL))
                prepare()
            }
            start()
        }
    }

    override fun pauseAudio() {
        player.apply {
            pause()
            length = currentPosition;
        }
    }

    override fun previousAudio(songURL: String?) {
        length = 0
        playAudio(songURL)
    }

    override fun nextAudio(songURL: String?) {
        length = 0
        playAudio(songURL)
    }

    override fun updateAudioProgress(progress:Int){
        length = progress * 1000
        player.seekTo(length)
    }
}