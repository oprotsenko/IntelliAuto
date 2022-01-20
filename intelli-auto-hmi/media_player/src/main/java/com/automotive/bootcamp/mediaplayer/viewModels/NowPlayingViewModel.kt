package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.domain.models.SongWrapper
import com.automotive.bootcamp.mediaplayer.domain.useCases.*

class NowPlayingViewModel(private val playerCommandRunner: MediaPlayerCommandRunner) : ViewModel() {
    private var albumsListData = mutableListOf<SongWrapper>()

    private val _isPlaying: MutableLiveData<Boolean> = MutableLiveData()
    private val _currentSong: MutableLiveData<SongWrapper> = MutableLiveData()

    private var position: Int = 0

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    val currentSong: LiveData<SongWrapper>
        get() = _currentSong

    fun setAlbumsListData(albumsListData: List<SongWrapper>?) {
        albumsListData?.let {
            this.albumsListData.clear()
            this.albumsListData.addAll(it)
        }
    }

    fun setPosition(position: Int) {
        this.position = position

        _currentSong.value = albumsListData[position]
    }

    fun playSong() {
        _currentSong.value?.let {
            playerCommandRunner.playSong(it.song.songURL)
            _isPlaying.value = true
        }
    }

    fun pauseSong() {
        if (_isPlaying.value == true) {
            playerCommandRunner.pauseSong()
            _isPlaying.value = false
        }
    }

    fun nextSong() {
        if (++position == albumsListData.size) {
            position = 0;
        }

        albumsListData.let {
            val media = it[position]
            playerCommandRunner.nextSong(media.song.songURL)
            _currentSong.value = media
            _isPlaying.value = true
        }
    }

    fun previousSong() {
        if (--position < 0) {
            albumsListData.let {
                position = it.size - 1
            }
        }

        albumsListData.let {
            val media = it[position]
            playerCommandRunner.previousSong(media.song.songURL)
            _currentSong.value = media
            _isPlaying.value = true
        }
    }

    fun shuffleSongs() {
        currentSong.value?.let {
            val albumsListMinusCurrentSong = albumsListData.minus(it)
            val shuffledAlbumsList = albumsListMinusCurrentSong.shuffled()

            position = 0
            albumsListData.clear()
            albumsListData.add(it)
            albumsListData.addAll(shuffledAlbumsList)
        }
    }

    fun repeatOneSong() {
        //  shuffleSongs.e
    }
}