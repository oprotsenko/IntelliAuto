package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.content.*
import android.os.IBinder
import android.util.Log
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.utils.service.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioServiceConnectionListener

class AudioPlaybackControl(private val context: Context) : ServiceConnection {
    private var audioPlayerService: AudioPlayerService? = null
    private var audioServiceConnectionListener: AudioServiceConnectionListener? = null
    private var serviceBound = false
    private var audioUrl: String? = null

    fun setOnAudioServiceConnectionListener(audioServiceConnectionListener: AudioServiceConnectionListener) {
        this.audioServiceConnectionListener = audioServiceConnectionListener
    }

    fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener) =
        audioPlayerService?.setOnAudioCompletionListener(audioCompletionListener)

    fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener) =
        audioPlayerService?.setOnAudioRunningListener(audioRunningListener)

    fun playAudio(audioUrl: String?) {
        if (serviceBound) {
            audioPlayerService?.playAudio(audioUrl)
        } else {
            this.audioUrl = audioUrl
            context.let {
                Intent(it, AudioPlayerService::class.java).also { intent->
                    it.startService(intent)
                    it.bindService(intent, this, Context.BIND_AUTO_CREATE)
                }
            }
        }
    }

    fun pauseAudio() = audioPlayerService?.pauseAudio()

    fun updateAudioProgress(progress: Int) = audioPlayerService?.updateAudioProgress(progress)

    fun getShuffledAudioList(
        audioListData: MutableList<AudioWrapper>,
        currentAudio: AudioWrapper
    ): MutableList<AudioWrapper> {
        val audioListMinusCurrentAudio = audioListData.filter {
            it != currentAudio
        }
        val shuffledAudioList = audioListMinusCurrentAudio.shuffled()

        audioListData.clear()
        audioListData.add(currentAudio)
        audioListData.addAll(shuffledAudioList)

        return audioListData
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder: AudioPlayerService.LocalBinder = service as AudioPlayerService.LocalBinder
        audioPlayerService = binder.service
        audioServiceConnectionListener?.onAudioServiceConnected()
        audioPlayerService?.playAudio(audioUrl)
        serviceBound = true
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        serviceBound = false
    }
}