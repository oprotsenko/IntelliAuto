package com.automotive.bootcamp.mediaplayer.utils.basicService

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.AudioPlaybackControl
import org.koin.android.ext.android.inject

class AudioPlayerService : Service() {
    private val iBinder = LocalBinder()

    private val playbackControl: AudioPlaybackControl by inject()

    val currentAudio = playbackControl.currentAudio
    val isPlaying = playbackControl.isPlaying
    val isShuffled = playbackControl.isShuffled
    val repeatMode = playbackControl.repeatMode
    val currentAudioDuration = playbackControl.currentAudioDuration
    val currentAudioProgress = playbackControl.currentAudioProgress

    inner class LocalBinder : Binder() {
        val service: AudioPlayerService
            get() = this@AudioPlayerService
    }

    override fun onBind(p0: Intent?): IBinder {
        return iBinder
    }

    fun init(playlist: PlaylistWrapper, position: Int) {
        playbackControl.init(playlist, position)
    }

    fun playAudio() {
        playbackControl.playAudio()
    }

    fun pauseAudio() {
        playbackControl.pauseAudio()
    }

    fun nextAudio() {
        playbackControl.nextAudio()
    }

    fun previousAudio() {
        playbackControl.previousAudio()
    }

    fun shuffleAudio() {
        playbackControl.shuffleAudio()
    }

    fun nextRepeatMode() {
        playbackControl.nextRepeatMode()
    }

    fun updateAudioProgress(progress: Int) {
        playbackControl.updateAudioProgress(progress)
    }

    fun clear(){
        playbackControl.clear()
    }
}