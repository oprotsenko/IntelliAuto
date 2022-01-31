package com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.utils.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NowPlayingViewModel(
    private val audioPlaybackControl: AudioPlaybackControl,
    private val addRecent: AddRecent,
    retrieveRecentAudio: RetrieveRecentAudio
) : ViewModel(),
    AudioCompletionListener, AudioRunningListener {
    private var audioListData = mutableListOf<AudioWrapper>()
    private var originalAudioListData = mutableListOf<AudioWrapper>()
    private val recentAudioFlow: Flow<List<AudioWrapper>?>? = retrieveRecentAudio.retrieveRecentAudio()
    private var recentAudio: List<AudioWrapper>? = null

    private val _isPlaying by lazy { MutableLiveData<Boolean>() }
    private val _isShuffled by lazy { MutableLiveData<Boolean>() }
    private val _repeatMode by lazy { MutableLiveData<RepeatMode>() }
    private val _currentAudio by lazy { MutableLiveData<AudioWrapper>() }
    private val _currentAudioDuration by lazy { MutableLiveData<Int>() }
    private val _currentAudioProgress by lazy { MutableLiveData<Int>() }

    private var position: Int = 0

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

    init {
        viewModelScope.launch {
            recentAudioFlow?.collect {
                recentAudio = it
            }
        }
    }

    fun init(playlist: PlaylistWrapper, position: Int) {
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

        audioPlaybackControl.setOnAudioCompletionListener(this)
        audioPlaybackControl.setOnAudioRunningListener(this)
    }

    fun playAudio() {
        _currentAudio.value?.let {
            audioPlaybackControl.playAudio(it.audio.url)
            _isPlaying.value = true

            viewModelScope.launch {
                addRecent.execute(it.audio.id, recentAudio?.map { audio ->
                    audio.unwrap()
                })
            }
        }
    }

    fun pauseAudio() {
        if (_isPlaying.value == true) {
            audioPlaybackControl.pauseAudio()
            _isPlaying.value = false
        }
    }

    fun nextAudio() {
        if (++position == audioListData.size) {
            position = 0;
        }

        val audioWrapped = audioListData[position]
        audioPlaybackControl.nextAudio(audioWrapped.audio.url)
        _currentAudio.value = audioWrapped
        _isPlaying.value = true

        viewModelScope.launch {
            addRecent.execute(audioWrapped.audio.id, recentAudio?.map { audio ->
                audio.unwrap()
            })
        }
    }

    fun previousAudio() {
        if (--position < 0) {
            audioListData.let {
                position = it.size - 1
            }
        }

        val audioWrapped = audioListData[position]
        audioPlaybackControl.previousAudio(audioWrapped.audio.url)
        _currentAudio.value = audioWrapped
        _isPlaying.value = true

        viewModelScope.launch {
            addRecent.execute(audioWrapped.audio.id, recentAudio?.map { audio ->
                audio.unwrap()
            })
        }
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
            audioListData = audioPlaybackControl.getShuffledAudioList(audioListData, it)
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

    fun updateAudioProgress(progress: Int) {
        audioPlaybackControl.updateAudioProgress(progress)
    }

    override fun onAudioCompletion() {
        when (_repeatMode.value) {
            RepeatMode.DEFAULT -> {
                if (_currentAudio.value != audioListData.last()) {
                    nextAudio()
                }
            }
            RepeatMode.REPEAT_ONE -> {
                playAudio()
            }
            RepeatMode.REPEAT_PLAYLIST -> {
                nextAudio()
            }
            else -> {}
        }
    }

    override fun onAudioRunning(duration: Int, currentProgress: Int) {
        _currentAudioDuration.value = duration
        _currentAudioProgress.value = currentProgress
    }
}