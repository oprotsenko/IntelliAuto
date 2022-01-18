package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.utils.MusicPlayer

class PreviousSong (private val musicPlayer: MusicPlayer){
    fun execute(songUrl:String?) = musicPlayer.previousSong(songUrl)
}