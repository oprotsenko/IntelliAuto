package com.automotive.bootcamp.mediaplayer.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRecent
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.utils.player.ExoAudioPlayer

class AudioPlaybackControl(
    context: Context,
    private val addRecent: AddRecent
) : AudioCompletionListener, AudioRunningListener {

    private val audioPlayer = ExoAudioPlayer(context)
    private var audiosList = mutableListOf<Audio>()
    private var originalAudiosList = mutableListOf<Audio>()

    private val _isPlaying by lazy { MutableLiveData<Boolean>() }
    private val _isShuffled by lazy { MutableLiveData<Boolean>() }
    private val _repeatMode by lazy { MutableLiveData<RepeatMode>() }
    private val _currentAudio by lazy { MutableLiveData<Audio>() }
    private val _currentAudioDuration by lazy { MutableLiveData<Int>() }
    private val _currentAudioProgress by lazy { MutableLiveData<Int>() }

    val currentAudio: LiveData<Audio>
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

    init {
        audioPlayer.setOnAudioCompletionListener(this)
        audioPlayer.setOnAudioRunningListener(this)
    }

    fun init(playlist: PlaylistWrapper, id: Long) {
        playlist.playlist.list?.let {
            audiosList.clear()
            originalAudiosList.clear()
            audiosList.addAll(it)
            originalAudiosList.addAll(it)

            _currentAudio.value = it.first { audio ->
                audio.id == id
            }.also { audio ->
                position = it.indexOf(audio)
            }
        }

        _isShuffled.value = false
        _repeatMode.value = RepeatMode.DEFAULT
    }

    fun playAudio() {
        _currentAudio.value?.let {
            audioPlayer.playAudio(it.url)
            addRecent.execute(it.id)
            _isPlaying.value = true
        }
    }

    fun pauseAudio() {
        if (_isPlaying.value == true) {
            audioPlayer.pauseAudio()
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
        audioListData: MutableList<Audio>,
        currentAudio: Audio
    ): MutableList<Audio> {
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
            else -> {
            }
        }
    }

    fun updateAudioProgress(progress: Int) {
        audioPlayer.updateAudioProgress(progress)
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

    fun onServiceDestroy() {
        addRecent.onServiceDestroy()
    }
}