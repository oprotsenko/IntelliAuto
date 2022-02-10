package com.automotive.bootcamp.mediaplayer.utils

interface AudioRunningListener {
    fun onAudioRunning(duration:Int, currentProgress:Int)
}