package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic

class MediaPlayerListingViewModel(private val getLocalMusic: GetLocalMusic) : ViewModel() {

    val albumsListData by lazy { MutableLiveData<MutableList<Song>>() }

//    init {
//        viewModelScope.launch {
//            albumsListData.value = getLocalMusic.getAlbums()
//        }
//    }

    init {
        albumsListData.value = mutableListOf()
    }
}