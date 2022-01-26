package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.SingleLiveEvent
import com.automotive.bootcamp.mediaplayer.domain.useCases.CreatePlaylist
import com.automotive.bootcamp.mediaplayer.domain.useCases.DeletePlaylist
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrievePlaylistAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val retrievePlaylistAudio: RetrievePlaylistAudio,
    private val createPlaylist: CreatePlaylist,
    private val deletePlaylist: DeletePlaylist,
) : ViewModel() {

    val playlistsData by lazy { MutableLiveData<List<PlaylistWrapper>>() }
    val selectedPlaylist by lazy { SingleLiveEvent<PlaylistWrapper>() }

    init {
        viewModelScope.launch {
            playlistsData.value = retrievePlaylistAudio.retrievePlaylists()
        }
    }

    fun openPlaylist(position: Int) {
        viewModelScope.launch {
            selectedPlaylist.value =
                playlistsData.value?.let { retrievePlaylistAudio.retrievePlaylist(it[position].playlist.id) }
        }
    }
}