package com.automotive.bootcamp.mediaplayer.utils

import com.automotive.bootcamp.mediaplayer.presentation.SongCompletionListener

interface MusicPlayer {
    fun setOnSongCompletionListener(songCompletionListener: SongCompletionListener)
    fun play(songURL: String?)
    fun pause()
    fun previousSong(songURL: String?)
    fun nextSong(songURL: String?)
}