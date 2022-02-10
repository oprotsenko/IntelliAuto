package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.utils.AudioPlaybackControl

class QuickPlaybackControlsViewModel(
    private val audioPlaybackControl: AudioPlaybackControl
) : ViewModel() {

    val currentAudio = audioPlaybackControl.currentAudio
    val isPlaying = audioPlaybackControl.isPlaying
    val isShuffled = audioPlaybackControl.isShuffled
    val repeatMode = audioPlaybackControl.repeatMode
    val currentAudioDuration = audioPlaybackControl.currentAudioDuration
    val currentAudioProgress = audioPlaybackControl.currentAudioProgress

    fun playAudio() {
        audioPlaybackControl.playAudio()
    }

    fun pauseAudio() {
        audioPlaybackControl.pauseAudio()
    }

    fun nextAudio() {
        audioPlaybackControl.nextAudio()
    }

    fun previousAudio() {
        audioPlaybackControl.previousAudio()
    }

    fun shuffleAudio() {
        audioPlaybackControl.shuffleAudio()
    }

    fun nextRepeatMode() {
        audioPlaybackControl.nextRepeatMode()
    }

    fun updateAudioProgress(progress: Int) {
        audioPlaybackControl.updateAudioProgress(progress)
    }

    override fun onCleared() {
        audioPlaybackControl.clear()
        super.onCleared()
    }
}