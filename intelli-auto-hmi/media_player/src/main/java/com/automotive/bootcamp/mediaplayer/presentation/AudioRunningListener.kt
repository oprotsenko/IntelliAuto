package com.automotive.bootcamp.mediaplayer.presentation

interface AudioRunningListener {
    fun onAudioRunning(duration:Int, currentProgress:Int)
}