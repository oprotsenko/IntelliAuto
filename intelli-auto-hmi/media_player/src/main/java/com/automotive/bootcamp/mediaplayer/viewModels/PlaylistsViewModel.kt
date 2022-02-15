package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.*
import com.automotive.bootcamp.mediaplayer.domain.useCases.DeletePlaylist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val managePlaylists: ManagePlaylists,
    private val deletePlaylist: DeletePlaylist,
) : ViewModel() {
    val playlistsData: LiveData<List<PlaylistWrapper>?> =
        managePlaylists.getAllPlaylists().asLiveData().map { list ->
            list?.map { playlist ->
                playlist.mapToPlaylistWrapper()
            }
        }
    val selectedPlaylist by lazy { MutableLiveData<PlaylistWrapper>() }
    val createPlaylistView by lazy { MutableLiveData<Boolean>() }

    fun openPlaylist(pid: Long) {
        playlistsData.value?.first{
            it.playlist.id == pid
        }.run {
            selectedPlaylist.value = this
        }
    }

    fun removePlaylist(pid: Long) {
        viewModelScope.launch {
            deletePlaylist.deletePlaylist(pid)
        }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch {
            managePlaylists.createPlaylist(playlistName)
        }
    }
}