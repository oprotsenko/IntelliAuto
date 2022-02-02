package com.automotive.bootcamp.mediaplayer.utils.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener
import org.koin.android.ext.android.get

class AudioPlayerService : Service() {
    private val iBinder = LocalBinder()
    private val audioPlayer: AudioPlayer = get()

    inner class LocalBinder : Binder() {
        val service: AudioPlayerService
            get() = this@AudioPlayerService
    }

    override fun onBind(p0: Intent?): IBinder {
        return iBinder
    }

    override fun onDestroy() {
        audioPlayer.stop()
        super.onDestroy()
    }

    fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener) {
        audioPlayer.setOnAudioCompletionListener(audioCompletionListener)
    }

    fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener) {
        audioPlayer.setOnAudioRunningListener(audioRunningListener)
    }

    fun playAudio(url: String?) {
        audioPlayer.playAudio(url)
    }

    fun pauseAudio() {
        audioPlayer.pauseAudio()
    }

    fun updateAudioProgress(progress: Int) {
        audioPlayer.updateAudioProgress(progress)
    }
}