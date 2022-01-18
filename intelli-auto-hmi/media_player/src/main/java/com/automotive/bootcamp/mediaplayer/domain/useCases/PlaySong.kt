package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.utils.MusicPlayer

class PlaySong(private val musicPlayer: MusicPlayer) {
    fun execute(songUrl:String?) = musicPlayer.play(songUrl)
}