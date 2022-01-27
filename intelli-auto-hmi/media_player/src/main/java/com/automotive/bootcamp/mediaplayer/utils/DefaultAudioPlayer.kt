package com.automotive.bootcamp.mediaplayer.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.automotive.bootcamp.mediaplayer.utils.extensions.currentSeconds
import com.automotive.bootcamp.mediaplayer.utils.extensions.seconds
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener

class DefaultAudioPlayer(private val context: Context) : AudioPlayer {
    private val player: MediaPlayer by lazy { MediaPlayer() }
    private lateinit var audioCompletionListener: AudioCompletionListener
    private lateinit var audioRunningListener: AudioRunningListener
    private lateinit var runnable: Runnable

    private var lastAudioUrl: String? = null

    init {
        player.setOnCompletionListener {
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

    override fun playAudio(url: String?) {
        if (lastAudioUrl != url) {
            player.apply {
                reset()
                setDataSource(context, Uri.parse(url))
                prepare()
                start()
            }
        } else {
            if (!player.isPlaying) {
                player.apply {
                    seekTo(player.currentPosition)
                    start()
                }
            }
        }
        lastAudioUrl = url
    }

    override fun pauseAudio() {
        player.pause()
    }

    override fun previousAudio(url: String?) {
        playAudio(url)
    }

    override fun nextAudio(url: String?) {
        playAudio(url)
    }

    override fun updateAudioProgress(progress: Int) {
        player.seekTo(progress * 1000)
    }
}