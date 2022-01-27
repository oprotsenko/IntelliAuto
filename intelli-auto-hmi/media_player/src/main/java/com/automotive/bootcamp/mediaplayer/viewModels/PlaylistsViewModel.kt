package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.SingleLiveEvent
import com.automotive.bootcamp.mediaplayer.domain.useCases.DeletePlaylist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrievePlaylistAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val retrievePlaylistAudio: RetrievePlaylistAudio,
    private val managePlaylists: ManagePlaylists,
    private val deletePlaylist: DeletePlaylist,
) : ViewModel() {

    val playlistsData by lazy { MutableLiveData<List<PlaylistWrapper>>() }
    val selectedPlaylist by lazy { MutableLiveData<PlaylistWrapper>() }
    val createPlaylistView by lazy { MutableLiveData<Boolean>() }


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

    fun removePlaylist(position: Int) {
        viewModelScope.launch {
            playlistsData.value?.let { deletePlaylist.deletePlaylist(it[position].playlist.id) }
            playlistsData.value = retrievePlaylistAudio.retrievePlaylists()
        }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch {
            managePlaylists.createPlaylist(playlistName)
            playlistsData.value = retrievePlaylistAudio.retrievePlaylists()
        }
    }
}