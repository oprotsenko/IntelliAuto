package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.utils.MusicPlayer

class NextSong (private val musicPlayer: MusicPlayer){
    fun execute(songUrl:String?) = musicPlayer.nextSong(songUrl)
}