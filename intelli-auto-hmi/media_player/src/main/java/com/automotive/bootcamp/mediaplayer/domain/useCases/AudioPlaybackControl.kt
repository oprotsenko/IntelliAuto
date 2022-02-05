package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.content.*
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.utils.basicService.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioServiceConnectionListener


class AudioPlaybackControl(
    private val context: Context,
//    private val audioServiceConnection: MusicServiceConnection
) : ServiceConnection {
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
                Intent(it, AudioPlayerService::class.java).also { intent ->
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

    //new
//    val isConnected = audioServiceConnection.isConnected
//    val networkError = audioServiceConnection.networkError
//    val currentPlayingAudio = audioServiceConnection.currentPlayingAudio
//    val playbackState = audioServiceConnection.playbackState

    fun skipToNextAudio() {
//        audioServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousAudio() {
//        audioServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
//        audioServiceConnection.transportControls.seekTo(pos)
    }

    fun playOrToggleAudio(mediaItem: AudioWrapper, toggle: Boolean = false) {
//        val isPrepared = playbackState.value?.isPrepared ?: false

//        if (isPrepared && mediaItem.audio.id.toString() == currentPlayingAudio.value?.getString(METADATA_KEY_MEDIA_ID)) {
//            playbackState.value?.let { playbackState ->
//                when {
//                    playbackState.isPlaying -> if (toggle) audioServiceConnection.transportControls.pause()
//                    playbackState.isPlayEnabled -> audioServiceConnection.transportControls.play()
//                    else -> Unit
//                }
//            }
//        } else {
//            audioServiceConnection.transportControls.playFromMediaId(mediaItem.audio.id.toString(), null)
//        }
    }
    //
}