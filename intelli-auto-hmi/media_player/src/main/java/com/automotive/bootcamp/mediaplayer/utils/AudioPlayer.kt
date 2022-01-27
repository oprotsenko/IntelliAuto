package com.automotive.bootcamp.mediaplayer.utils

import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener

interface AudioPlayer {
    fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener)
    fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener)

    fun playAudio(url: String?)
    fun pauseAudio()
    fun previousAudio(url: String?)
    fun nextAudio(url: String?)
    fun updateAudioProgress(progress: Int)
}