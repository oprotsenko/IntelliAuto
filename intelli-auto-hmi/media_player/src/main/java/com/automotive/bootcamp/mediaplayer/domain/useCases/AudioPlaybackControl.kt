package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.content.*
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.basicService.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.utils.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class AudioPlaybackControl(
    private val context: Context,
    private val addRecent: AddRecent,
    retrieveRecentAudio: RetrieveRecentAudio
) : AudioCompletionListener, AudioRunningListener, ServiceConnection {
    private var audioListData = mutableListOf<AudioWrapper>()
    private var originalAudioListData = mutableListOf<AudioWrapper>()
    private val recentAudioFlow: Flow<List<AudioWrapper>?>? =
        retrieveRecentAudio.retrieveRecentAudio()
    private var recentAudio: List<AudioWrapper>? = null

    private val _isPlaying by lazy { MutableLiveData<Boolean>() }
    private val _isShuffled by lazy { MutableLiveData<Boolean>() }
    private val _repeatMode by lazy { MutableLiveData<RepeatMode>() }
    private val _currentAudio by lazy { MutableLiveData<AudioWrapper>() }
    private val _currentAudioDuration by lazy { MutableLiveData<Int>() }
    private val _currentAudioProgress by lazy { MutableLiveData<Int>() }

    val currentAudio: LiveData<AudioWrapper>
        get() = _currentAudio

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    val isShuffled: LiveData<Boolean>
        get() = _isShuffled

    val repeatMode: LiveData<RepeatMode>
        get() = _repeatMode

    val currentAudioDuration: LiveData<Int>
        get() = _currentAudioDuration

    val currentAudioProgress: LiveData<Int>
        get() = _currentAudioProgress

    private var position: Int = 0

    suspend fun init(playlist: PlaylistWrapper, position: Int) {
        this.position = position
        val audioList = playlist.playlist.list?.map { audio ->
            audio.wrapAudio()
        }
        audioListData.clear()

        if (audioList != null) {
            audioListData.addAll(audioList)
        }

        originalAudioListData.clear()

        if (audioList != null) {
            originalAudioListData.addAll(audioList)
        }

        _currentAudio.value = audioListData[position]

        _isShuffled.value = false
        _repeatMode.value = RepeatMode.DEFAULT

        recentAudioFlow?.collect {
            recentAudio = it
        }
    }

    suspend fun playAudio() {
        _currentAudio.value?.let {
            playAudio(it.audio.url)
            _isPlaying.value = true

            addRecent.execute(it.audio.id, recentAudio?.map { audio ->
                audio.unwrap()
            })
        }
    }

    fun pauseAudio() {
        if (_isPlaying.value == true) {
            audioPlayerService?.pauseAudio()
            _isPlaying.value = false
        }
    }

    suspend fun nextAudio() {
        if (++position == audioListData.size) {
            position = 0;
        }

        val audioWrapped = audioListData[position]
        playAudio(audioWrapped.audio.url)
        _currentAudio.value = audioWrapped
        _isPlaying.value = true

        addRecent.execute(audioWrapped.audio.id, recentAudio?.map { audio ->
            audio.unwrap()
        })
    }

    suspend fun previousAudio() {
        if (--position < 0) {
            audioListData.let {
                position = it.size - 1
            }
        }

        val audioWrapped = audioListData[position]
        playAudio(audioWrapped.audio.url)
        _currentAudio.value = audioWrapped
        _isPlaying.value = true

        addRecent.execute(audioWrapped.audio.id, recentAudio?.map { audio ->
            audio.unwrap()
        })
    }

    fun shuffleAudio() {
        if (_isShuffled.value == true) {
            setOriginalList()

            _isShuffled.value = false
        } else {
            setShuffledList()

            _isShuffled.value = true
        }
    }

    private fun setOriginalList() {
        audioListData = originalAudioListData
        position = audioListData.indexOf(currentAudio.value)
    }

    private fun setShuffledList() {
        position = 0
        currentAudio.value?.let {
            audioListData = getShuffledAudioList(audioListData, it)
        }
    }

    fun nextRepeatMode() {
        when (_repeatMode.value) {
            RepeatMode.DEFAULT -> {
                _repeatMode.value = RepeatMode.REPEAT_ONE
            }
            RepeatMode.REPEAT_ONE -> {
                _repeatMode.value = RepeatMode.REPEAT_PLAYLIST
            }
            RepeatMode.REPEAT_PLAYLIST -> {
                _repeatMode.value = RepeatMode.DEFAULT
            }
            else -> {}
        }
    }

    override fun onAudioCompletion() {
//        when (_repeatMode.value) {
//            RepeatMode.DEFAULT -> {
//                if (_currentAudio.value != audioListData.last()) {
//                    nextAudio()
//                }
//            }
//            RepeatMode.REPEAT_ONE -> {
//                playAudio()
//            }
//            RepeatMode.REPEAT_PLAYLIST -> {
//                nextAudio()
//            }
//            else -> Unit
//        }
    }

    override fun onAudioRunning(duration: Int, currentProgress: Int) {
        _currentAudioDuration.value = duration
        _currentAudioProgress.value = currentProgress
    }


    private var audioPlayerService: AudioPlayerService? = null
    private var serviceBound = false
    private var audioUrl: String? = null

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

    fun updateAudioProgress(progress: Int) = audioPlayerService?.updateAudioProgress(progress)

    private fun getShuffledAudioList(
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
        audioPlayerService?.setOnAudioCompletionListener(this)
        audioPlayerService?.setOnAudioRunningListener(this)
        audioPlayerService?.playAudio(audioUrl)
        serviceBound = true
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        serviceBound = false
    }
}