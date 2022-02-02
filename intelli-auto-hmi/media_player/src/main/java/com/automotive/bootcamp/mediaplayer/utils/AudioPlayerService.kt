package com.automotive.bootcamp.mediaplayer.utils

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import com.automotive.bootcamp.mediaplayer.domain.useCases.AUDIO_SERVICE_INTENT_EXTRA
import com.automotive.bootcamp.mediaplayer.domain.useCases.BROADCAST_PLAY_AUDIO
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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val songUrl = intent.extras?.getString(AUDIO_SERVICE_INTENT_EXTRA)
        playAudio(songUrl)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        registerPlayAudioReceiver()
        super.onCreate()
    }

    override fun onDestroy() {
        audioPlayer.stop()
        unregisterReceiver(playAudioReceiver)
        super.onDestroy()
    }

    private fun registerPlayAudioReceiver() {
        val filter = IntentFilter(BROADCAST_PLAY_AUDIO)
        registerReceiver(playAudioReceiver, filter)
    }

    private val playAudioReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val songUrl = intent?.extras?.getString(AUDIO_SERVICE_INTENT_EXTRA)
            playAudio(songUrl)
        }
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