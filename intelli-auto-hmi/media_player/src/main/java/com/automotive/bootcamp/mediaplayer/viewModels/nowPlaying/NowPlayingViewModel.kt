package com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.utils.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class NowPlayingViewModel(private val playerCommandRunner: MediaPlayerCommandRunner) : ViewModel(),
    AudioCompletionListener, AudioRunningListener {
    private var audioListData = mutableListOf<AudioWrapper>()
    private var originalAudioListData = mutableListOf<AudioWrapper>()

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

    fun init(audioListData: List<AudioWrapper>?, position: Int) {
        this.position = position
        audioListData?.let {
            this.audioListData.clear()
            this.audioListData.addAll(it)

            originalAudioListData.clear()
            originalAudioListData.addAll(it)

            _currentAudio.value = it[position]
        }

        _isShuffled.value = false
        _repeatMode.value = RepeatMode.DEFAULT

        playerCommandRunner.setOnAudioCompletionListener(this)
        playerCommandRunner.setOnAudioRunningListener(this)
    }

    fun playAudio() {
        _currentAudio.value?.let {
            playerCommandRunner.playAudio(it.audio.songURL)
            _isPlaying.value = true
        }
    }

    fun pauseAudio() {
        if (_isPlaying.value == true) {
            playerCommandRunner.pauseAudio()
            _isPlaying.value = false
        }
    }

    fun nextAudio() {
        if (++position == audioListData.size) {
            position = 0;
        }

        val media = audioListData[position]
        playerCommandRunner.nextAudio(media.audio.songURL)
        _currentAudio.value = media
        _isPlaying.value = true
    }

    fun previousAudio() {
        if (--position < 0) {
            audioListData.let {
                position = it.size - 1
            }
        }

        val media = audioListData[position]
        playerCommandRunner.previousAudio(media.audio.songURL)
        _currentAudio.value = media
        _isPlaying.value = true
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
            audioListData = playerCommandRunner.getShuffledAudioList(audioListData, it)
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

    fun updateAudioProgress(progress:Int) {
        playerCommandRunner.updateAudioProgress(progress)
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