package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.domain.useCases.DeletePlaylist
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrievePlaylists
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val retrievePlaylists: RetrievePlaylists,
    private val deletePlaylist: DeletePlaylist
) : ViewModel() {

    val playlistsData by lazy { MutableLiveData<List<PlaylistWrapper>>() }

    init {
        viewModelScope.launch {
            playlistsData.value = retrievePlaylists.retrievePlaylists()
        }
    }
}