package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.content.*
import android.os.IBinder
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioServiceConnectionListener

const val BROADCAST_PLAY_AUDIO = "com.automotive.bootcamp.mediaplayer.PlayAudio"
const val AUDIO_SERVICE_INTENT_EXTRA = "audioUrl"

class AudioPlaybackControl(private val context: Context) : ServiceConnection {
    private var audioPlayerService: AudioPlayerService? = null
    private var audioServiceConnectionListener: AudioServiceConnectionListener? = null
    var serviceBound = false

    fun setOnAudioServiceConnectionListener(audioServiceConnectionListener: AudioServiceConnectionListener) {
        this.audioServiceConnectionListener = audioServiceConnectionListener
    }

    fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener) =
        audioPlayerService?.setOnAudioCompletionListener(audioCompletionListener)

    fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener) =
        audioPlayerService?.setOnAudioRunningListener(audioRunningListener)

    fun playAudio(audioUrl: String?) {
        if (serviceBound) {
            val intent = Intent(BROADCAST_PLAY_AUDIO)
            intent.putExtra(AUDIO_SERVICE_INTENT_EXTRA, audioUrl)
            context.sendBroadcast(intent)
        } else {
            val intent = Intent(context, AudioPlayerService::class.java)
            intent.putExtra(AUDIO_SERVICE_INTENT_EXTRA, audioUrl)
            context.startService(intent)
            context.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
    }

    fun pauseAudio() {
        audioPlayerService?.pauseAudio()
    }

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
        serviceBound = true
        audioServiceConnectionListener?.onAudioServiceConnected()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        serviceBound = false
    }
}