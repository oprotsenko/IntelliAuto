package com.automotive.bootcamp.mediaplayer.utils

interface MusicPlayer {
    fun play(songURL: String?)
    fun pause()
    fun previousSong(songURL: String?)
    fun nextSong(songURL: String?)
}