package com.automotive.bootcamp.mediaplayer.utils

import com.automotive.bootcamp.mediaplayer.presentation.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.presentation.AudioRunningListener

interface AudioPlayer {
    fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener)
    fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener)

    fun playAudio(songURL: String?)
    fun pauseAudio()
    fun previousAudio(songURL: String?)
    fun nextAudio(songURL: String?)
    fun updateAudioProgress(progress: Int)
}