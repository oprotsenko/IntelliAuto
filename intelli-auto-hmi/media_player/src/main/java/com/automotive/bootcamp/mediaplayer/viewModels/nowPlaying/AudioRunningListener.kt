package com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying

interface AudioRunningListener {
    fun onAudioRunning(duration:Int, currentProgress:Int)
}