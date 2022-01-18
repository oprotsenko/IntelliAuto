package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.domain.useCases.*

class NowPlayingViewModel(
    private val playSong: PlaySong,
    private val pauseSong: PauseSong,
    private val nextSong: NextSong,
    private val previousSong: PreviousSong,
    private val shuffleSongs: ShuffleSongs,
    private val repeatOneSong: RepeatOneSong
) : ViewModel() {

    private val _isPlaying: MutableLiveData<Boolean> = MutableLiveData()

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    fun playSong(songUrl: String?) {
        playSong.execute(songUrl)
        _isPlaying.value = true
    }

    fun pauseSong() {
        if(_isPlaying.value == true){
            pauseSong.execute()
            _isPlaying.value = false
        }
    }

    fun nextSong(songUrl: String?) {
        nextSong.execute(songUrl)
        _isPlaying.value = true
    }

    fun previousSong(songUrl: String?) {
        previousSong.execute(songUrl)
        _isPlaying.value = true
    }

    fun shuffleSongs(){
      //  shuffleSongs.e
    }
}