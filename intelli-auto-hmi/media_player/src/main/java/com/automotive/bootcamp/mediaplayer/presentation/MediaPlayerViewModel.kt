package com.automotive.bootcamp.mediaplayer.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import com.automotive.bootcamp.mediaplayer.presentation.domain.useCases.GetLocalMusic
import kotlinx.coroutines.launch

class MediaPlayerViewModel(private val getLocalMusic: GetLocalMusic) : ViewModel() {

    val albumsListData by lazy { MutableLiveData<MutableList<MediaAlbum>>() }

//    init {
//        viewModelScope.launch {
//            albumsListData.value = getLocalMusic.getAlbums()
//        }
//    }

    init {
        albumsListData.value = mutableListOf()
    }
}