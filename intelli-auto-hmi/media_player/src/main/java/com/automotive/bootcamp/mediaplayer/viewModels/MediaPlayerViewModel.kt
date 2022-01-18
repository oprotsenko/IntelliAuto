package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic

class MediaPlayerViewModel(private val getLocalMusic: GetLocalMusic) : ViewModel() {

    val albumsListData by lazy { MutableLiveData<MutableList<Song>>() }

    val selected = MutableLiveData<Song>()

    init {
        albumsListData.value = mutableListOf()
    }

    fun select(position: Int) {
        val media = albumsListData.value?.get(position)

        media?.let {
            selected.value = it
        }
    }
}