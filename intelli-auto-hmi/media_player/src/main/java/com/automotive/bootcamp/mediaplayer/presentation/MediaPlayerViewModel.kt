package com.automotive.bootcamp.mediaplayer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.presentation.data.FakeAlbumsRepository
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import kotlinx.coroutines.launch

class MediaPlayerViewModel : ViewModel() {
    private val repository = FakeAlbumsRepository()

    val albumsList : LiveData<List<MediaAlbum>>

    init {
        albumsList = repository.albums
    }

    fun getAlbums(){
       viewModelScope.launch {
           repository.getAlbums()
       }
    }
}