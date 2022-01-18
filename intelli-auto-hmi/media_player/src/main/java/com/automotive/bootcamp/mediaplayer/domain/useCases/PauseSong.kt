package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.MusicPlayer

class PauseSong (private val musicPlayer: MusicPlayer) {
    fun execute() = musicPlayer.pause()
}