package com.automotive.bootcamp.mediaplayer.utils

import com.automotive.bootcamp.mediaplayer.domain.models.Song

interface MusicPlayer {
    fun play(songURL: String?)
    fun pause()
    fun previousSong(songURL: String?)
    fun nextSong(songURL: String?)
    fun shufflePlaylist(songs: ArrayList<Song>)
    fun repeatOne()
}