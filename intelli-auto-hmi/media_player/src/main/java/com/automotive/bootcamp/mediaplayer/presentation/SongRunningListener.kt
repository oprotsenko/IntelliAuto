package com.automotive.bootcamp.mediaplayer.presentation

interface SongRunningListener {
    fun onSongRunning(duration:Int, currentProgress:Int)
}