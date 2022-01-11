package com.automotive.bootcamp.mediaplayer.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.presentation.data.FakeAlbumsRepository
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import kotlinx.coroutines.launch

class MediaPlayerViewModel : ViewModel() {
    private val repository = FakeAlbumsRepository()

    val albumsListData by lazy { MutableLiveData<List<MediaAlbum>>() }

    init {
        viewModelScope.launch {
            albumsListData.value = repository.getAlbums()
        }
    }
}