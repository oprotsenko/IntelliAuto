package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.presentation.SongCompletionListener
import com.automotive.bootcamp.mediaplayer.utils.MusicPlayer

class MediaPlayerCommandRunner(private val musicPlayer: MusicPlayer) {
    fun setOnSongCompletionListener(songCompletionListener: SongCompletionListener) =
        musicPlayer.setOnSongCompletionListener(songCompletionListener)

    fun playSong(songUrl: String?) = musicPlayer.play(songUrl)
    fun pauseSong() = musicPlayer.pause()
    fun nextSong(songUrl: String?) = musicPlayer.nextSong(songUrl)
    fun previousSong(songUrl: String?) = musicPlayer.previousSong(songUrl)
}