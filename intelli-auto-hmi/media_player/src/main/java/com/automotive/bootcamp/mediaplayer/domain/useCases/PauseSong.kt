package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.utils.MusicPlayer

class PauseSong (private val musicPlayer: MusicPlayer) {
    fun execute() = musicPlayer.pause()
}