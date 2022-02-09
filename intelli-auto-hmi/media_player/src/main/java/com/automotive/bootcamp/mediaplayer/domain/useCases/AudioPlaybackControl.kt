package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.content.*
import android.os.IBinder
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class AudioPlaybackControl(
    private val context: Context,
    private val addRecent: AddRecent,
    retrieveRecentAudio: RetrieveRecentAudio
) : AudioCompletionListener, AudioRunningListener, ServiceConnection {
    private val job = Job()
    private val playbackScope = CoroutineScope(Dispatchers.IO + job)

    private var audioPlayerService: AudioPlayerService? = null
    private var serviceBound = false

    private var audiosList = mutableListOf<AudioWrapper>()
    private var originalAudiosList = mutableListOf<AudioWrapper>()
    private val recentAudiosFlow: Flow<List<AudioWrapper>?>? = retrieveRecentAudio.retrieveRecentAudio()
    private var recentAudios: List<AudioWrapper>? = null

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

    fun init(playlist: PlaylistWrapper, position: Int) {
        playbackScope.launch {
            recentAudiosFlow?.collect {
                recentAudios = it
            }
        }

        this.position = position

        val audioList = playlist.playlist.list?.map { audio ->
            audio.wrapAudio()
        }
        this.audiosList.clear()
        if (audioList != null) {
            this.audiosList.addAll(audioList)
        }

        originalAudiosList.clear()
        if (audioList != null) {
            originalAudiosList.addAll(audioList)
        }

        _currentAudio.value = this.audiosList[position]
        _isShuffled.value = false
        _repeatMode.value = RepeatMode.DEFAULT
    }

    fun playAudio() {
        _currentAudio.value?.let {
            playAudioWithService(it.audio.url)
            addToRecent()
            _isPlaying.value = true
        }
    }

    private fun addToRecent() {
        playbackScope.launch {
            _currentAudio.value?.let {
                addRecent.execute(it.audio.id, recentAudios?.map { audio ->
                    audio.unwrap()
                })
            }
        }
    }

    private fun playAudioWithService(audioUrl: String?) {
        if (serviceBound) {
            audioPlayerService?.playAudio(audioUrl)
        } else {
            context.let {
                Intent(it, AudioPlayerService::class.java).also { intent ->
                    it.startService(intent)
                    it.bindService(intent, this, Context.BIND_AUTO_CREATE)
                }
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder: AudioPlayerService.LocalBinder = service as AudioPlayerService.LocalBinder
        audioPlayerService = binder.service
        audioPlayerService?.setOnAudioCompletionListener(this)
        audioPlayerService?.setOnAudioRunningListener(this)
        audioPlayerService?.playAudio(_currentAudio.value?.audio?.url)
        serviceBound = true
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        serviceBound = false
    }

    fun pauseAudio() {
        if (_isPlaying.value == true) {
            audioPlayerService?.pauseAudio()
            _isPlaying.value = false
        }
    }

    fun nextAudio() {
        if (++position == audiosList.size) {
            position = 0;
        }

        val audioWrapped = audiosList[position]
        _currentAudio.value = audioWrapped

        playAudio()
    }

    fun previousAudio() {
        if (--position < 0) {
            audiosList.let {
                position = it.size - 1
            }
        }

        val audioWrapped = audiosList[position]
        _currentAudio.value = audioWrapped

        playAudio()
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
        audiosList = originalAudiosList
        position = audiosList.indexOf(currentAudio.value)
    }

    private fun setShuffledList() {
        position = 0
        currentAudio.value?.let {
            audiosList = getShuffledAudioList(audiosList, it)
        }
    }

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

    fun updateAudioProgress(progress: Int) {
        audioPlayerService?.updateAudioProgress(progress)
    }

    override fun onAudioCompletion() {
        when (_repeatMode.value) {
            RepeatMode.DEFAULT -> {
                if (_currentAudio.value != audiosList.last()) {
                    nextAudio()
                }
            }
            RepeatMode.REPEAT_ONE -> {
                playAudio()
            }
            RepeatMode.REPEAT_PLAYLIST -> {
                nextAudio()
            }
            else -> Unit
        }
    }

    override fun onAudioRunning(duration: Int, currentProgress: Int) {
        _currentAudioDuration.value = duration
        _currentAudioProgress.value = currentProgress
    }

    fun clear(){
        playbackScope.cancel()
    }
}