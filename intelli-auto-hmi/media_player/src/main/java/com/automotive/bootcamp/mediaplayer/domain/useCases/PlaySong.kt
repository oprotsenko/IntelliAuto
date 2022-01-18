package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.MusicPlayer

class PlaySong(private val musicPlayer: MusicPlayer) {
    fun execute(songUrl:String?) = musicPlayer.play(songUrl)
}