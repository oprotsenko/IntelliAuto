package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.utils.MusicPlayer

class MediaPlayerCommandRunner(private val musicPlayer: MusicPlayer) {
    fun playSong(songUrl:String?) = musicPlayer.play(songUrl)
    fun pauseSong() = musicPlayer.pause()
    fun nextSong(songUrl: String?) = musicPlayer.nextSong(songUrl)
    fun previousSong(songUrl:String?) = musicPlayer.previousSong(songUrl)
}