package com.automotive.bootcamp.mediaplayer.utils.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.automotive.bootcamp.mediaplayer.utils.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.utils.AudioRunningListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import java.util.concurrent.TimeUnit

class ExoAudioPlayer(context: Context) : AudioPlayer, Player.Listener {
    private var player = ExoPlayer.Builder(context).build()
    private var audioCompletionListener: AudioCompletionListener? = null
    private var audioRunningListener: AudioRunningListener? = null

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val delay = 1000L

    private var lastAudioUrl: String? = null

    init {
        player.addListener(this)
        initializeRunnable()
    }

    private fun initializeRunnable() {
        runnable = Runnable {
            TimeUnit.MILLISECONDS.let {
                val contentDuration = it.toSeconds(player.contentDuration).toInt()
                val currentPosition = it.toSeconds(player.currentPosition).toInt()
                audioRunningListener?.onAudioRunning(contentDuration, currentPosition)
            }
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

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when (playbackState) {
            Player.STATE_ENDED -> {
                lastAudioUrl = null
                audioCompletionListener?.onAudioCompletion()
            }
            Player.STATE_READY -> {
                player.play()
            }
        }
    }

    override fun playAudio(url: String?) {
        if (lastAudioUrl != url) {
            val mediaItem: MediaItem = MediaItem.fromUri(Uri.parse(url))

            player.apply {
                setMediaItem(mediaItem)
                prepare()
            }
        } else {
            if (!player.isPlaying) {
                player.apply {
                    seekTo(this.currentPosition)
                    play()
                }
            }
        }
        lastAudioUrl = url
    }

    override fun pauseAudio() {
        player.pause()
    }

    override fun updateAudioProgress(progress: Int) {
        player.seekTo(progress.toLong() * 1000)
    }

    override fun stop() {
        player.apply {
            stop()
            release()
        }
    }
}