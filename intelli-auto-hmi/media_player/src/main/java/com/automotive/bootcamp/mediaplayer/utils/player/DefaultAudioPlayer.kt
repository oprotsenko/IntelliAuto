package com.automotive.bootcamp.mediaplayer.utils.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.automotive.bootcamp.mediaplayer.utils.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.utils.AudioRunningListener
import com.automotive.bootcamp.mediaplayer.utils.extensions.currentSeconds
import com.automotive.bootcamp.mediaplayer.utils.extensions.seconds

class DefaultAudioPlayer(private val context: Context) : AudioPlayer,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private val player: MediaPlayer by lazy { MediaPlayer() }
    private var audioCompletionListener: AudioCompletionListener? = null
    private var audioRunningListener: AudioRunningListener? = null

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val delay = 1000L

    private var lastAudioUrl: String? = null

    init {
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)

        initializeRunnable()
    }

    private fun initializeRunnable() {
        runnable = Runnable {
            audioRunningListener?.onAudioRunning(player.seconds, player.currentSeconds)
            runnable?.let {
                handler.postDelayed(it, delay)
            }
        }
        runnable?.let {
            handler.postDelayed(it, delay)
        }
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
                prepareAsync()
            }
        } else {
            if (!player.isPlaying) {
                player.apply {
                    seekTo(this.currentPosition)
                    start()
                }
            }
        }
        lastAudioUrl = url
    }

    override fun onPrepared(mp: MediaPlayer?) {
        player.start()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        lastAudioUrl = null
        audioCompletionListener?.onAudioCompletion()
    }

    override fun pauseAudio() {
        player.pause()
    }

    override fun updateAudioProgress(progress: Int) {
        player.seekTo(progress * 1000)
    }

    override fun stop() {
        player.apply {
            stop()
            release()
        }
    }
}