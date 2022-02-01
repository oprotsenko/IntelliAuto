package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.*
import com.automotive.bootcamp.mediaplayer.domain.useCases.DeletePlaylist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val managePlaylists: ManagePlaylists,
    private val deletePlaylist: DeletePlaylist,
) : ViewModel() {
    val playlistsData: LiveData<List<PlaylistWrapper>?> = managePlaylists.getAllPlaylists().asLiveData()
    val selectedPlaylist by lazy { MutableLiveData<PlaylistWrapper>() }
    val createPlaylistView by lazy { MutableLiveData<Boolean>() }

    fun openPlaylist(position: Int) {
        selectedPlaylist.value = playlistsData.value?.get(position)
    }

    fun removePlaylist(position: Int) {
        viewModelScope.launch {
            playlistsData.value?.let {
                deletePlaylist.deletePlaylist(it[position].playlist.id)
            }
        }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch {
            managePlaylists.createPlaylist(playlistName)
        }
    }
}