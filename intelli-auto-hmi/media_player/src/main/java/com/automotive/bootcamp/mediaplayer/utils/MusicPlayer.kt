package com.automotive.bootcamp.mediaplayer.utils

import com.automotive.bootcamp.mediaplayer.presentation.SongCompletionListener
import com.automotive.bootcamp.mediaplayer.presentation.SongRunningListener

interface MusicPlayer {
    fun setOnSongCompletionListener(songCompletionListener: SongCompletionListener)
    fun setOnSongRunningListener(songRunningListener: SongRunningListener)

    fun play(songURL: String?)
    fun pause()
    fun previousSong(songURL: String?)
    fun nextSong(songURL: String?)
}