package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.MusicPlayer

class ShuffleSongs (private val musicPlayer: MusicPlayer){
    fun execute(songUrl:String?) = musicPlayer.previousSong(songUrl)
}