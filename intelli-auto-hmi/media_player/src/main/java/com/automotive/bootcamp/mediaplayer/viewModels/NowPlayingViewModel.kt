package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.SongWrapper
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.presentation.SongCompletionListener

class NowPlayingViewModel(private val playerCommandRunner: MediaPlayerCommandRunner) : ViewModel(),
    SongCompletionListener {
    private var albumsListData = mutableListOf<SongWrapper>()
    private var originalAlbumsListData = mutableListOf<SongWrapper>()

    private val _isPlaying by lazy { MutableLiveData<Boolean>() }
    private val _isShuffled by lazy { MutableLiveData<Boolean>() }
    private val _repeatMode by lazy { MutableLiveData<RepeatMode>() }
    private val _currentSong by lazy { MutableLiveData<SongWrapper>() }

    private var position: Int = 0

    val currentSong: LiveData<SongWrapper>
        get() = _currentSong

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    val isShuffled: LiveData<Boolean>
        get() = _isShuffled

    val repeatMode: LiveData<RepeatMode>
        get() = _repeatMode

    fun init(albumsListData: List<SongWrapper>?, position: Int) {
        this.position = position
        albumsListData?.let {
            this.albumsListData.clear()
            this.albumsListData.addAll(it)

            originalAlbumsListData.clear()
            originalAlbumsListData.addAll(it)

            _currentSong.value = it[position]
        }

        _isShuffled.value = false
        _repeatMode.value = RepeatMode.DEFAULT

        playerCommandRunner.setOnSongCompletionListener(this)
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
        if (_isShuffled.value == true) {
            setOriginalList()

            _isShuffled.value = false
        } else {
            setShuffledList()

            _isShuffled.value = true
        }
    }

    private fun setOriginalList() {
        albumsListData = originalAlbumsListData
        position = albumsListData.indexOf(currentSong.value)
    }

    private fun setShuffledList() {
        val albumsListMinusCurrentSong = albumsListData.filter {
            it != currentSong.value
        }
        val shuffledAlbumsList = albumsListMinusCurrentSong.shuffled()

        position = 0
        albumsListData.clear()

        currentSong.value?.let {
            albumsListData.add(it)
        }

        albumsListData.addAll(shuffledAlbumsList)
    }

    fun nextRepeatMode() {
        when (_repeatMode.value) {
            RepeatMode.DEFAULT -> {
                _repeatMode.value = RepeatMode.REPEAT_ONE
            }
            RepeatMode.REPEAT_ONE -> {
                _repeatMode.value = RepeatMode.REPEAT_PLAYLIST
            }
            RepeatMode.REPEAT_PLAYLIST -> {
                _repeatMode.value = RepeatMode.DEFAULT
            }
            else -> {}
        }
    }

    override fun onSongCompletion() {
        when (_repeatMode.value) {
            RepeatMode.DEFAULT -> {
                if (_currentSong.value != albumsListData.last()) {
                    nextSong()
                }
            }
            RepeatMode.REPEAT_ONE -> {
                playSong()
            }
            RepeatMode.REPEAT_PLAYLIST -> {
                nextSong()
            }
            else -> {}
        }
    }
}