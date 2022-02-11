package com.automotive.bootcamp.mediaplayer.utils.player

import com.automotive.bootcamp.mediaplayer.utils.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.utils.AudioRunningListener

interface AudioPlayer {
    fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener)
    fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener)

    fun playAudio(url: String?)
    fun pauseAudio()
    fun updateAudioProgress(progress: Int)
    fun stop()
}