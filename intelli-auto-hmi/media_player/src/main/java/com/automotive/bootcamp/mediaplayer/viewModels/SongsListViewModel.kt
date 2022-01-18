package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.base.CoroutineViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongsListViewModel(private val getLocalMusic: GetLocalMusic) : CoroutineViewModel() {


    val albumsListData by lazy { MutableLiveData<List<Song>>() }

    init {
        viewModelScope.launch {
            albumsListData.value = getLocalMusic.getLocalSongs()
        }
    }
}