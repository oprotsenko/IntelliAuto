package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageRecent
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class CustomPlaylistViewModel(
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {

    val customMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = null
    var dynamicallyAddAudioPosition: Int = 0
    var pid: Long? = null

    private suspend fun retrieveMusic(playlist: PlaylistWrapper?) {
        pid = playlist?.playlist?.id
        val list = playlist?.playlist?.list?.map {
            it.wrapAudio()
        }
        list?.map {
            it.isFavourite = manageFavourite.hasAudio(it.audio.id)
            it.isRecent = manageRecent.hasAudio(it.audio.id)
        }
        customMusicData.value = list
    }

    fun init(playlist: PlaylistWrapper?) {
        viewModelScope.launch {
            retrieveMusic(playlist)
            getAllPlaylists()
        }
    }

    fun removeFromCurrentPlaylist(position: Int) {
        viewModelScope.launch {
            customMusicData.value?.let {
                pid?.let { pid ->
                    managePlaylists.removeFromPlaylist(it[position].audio.id, pid)
                    customMusicData.value = managePlaylists.getPlaylist(pid)
                }
            }
        }
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            val list = customMusicData.value?.toMutableList()
            list?.let {
                if (manageFavourite.hasAudio(it[position].audio.id)) {
                    manageFavourite.removeFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = false)
                } else {
                    manageFavourite.addFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = true)
                }
            }
            customMusicData.value = list
        }
    }

    fun removeFromRecent(position: Int) {
        viewModelScope.launch {
            customMusicData.value?.let {
                val list = customMusicData.value?.toMutableList()
                list?.let { list[position] = list[position].copy(isRecent = false) }
                manageRecent.removeAudio(it[position].audio.id)
                customMusicData.value = list
            }
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = customMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(name = "name", list = it).mapToPlaylistWrapper() }
    }

    fun createPlaylist(playlistName: String, position: Int) {
        viewModelScope.launch {
            addToPlaylist(managePlaylists.createPlaylist(playlistName), position)
            getAllPlaylists()
        }
    }

    fun addToPlaylist(pid: Long, position: Int) {
        viewModelScope.launch {
            if (managePlaylists.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.id == pid) {
                customMusicData.value?.let {
                    if (!manageFavourite.hasAudio(it[position].audio.id)) {
                        val list = customMusicData.value?.toMutableList()
                        manageFavourite.addFavourite(it[position].audio.id)
                        list?.set(position, list[position].copy(isFavourite = true))
                        customMusicData.value = list
                    }
                }
            }
            customMusicData.value?.let {
                managePlaylists.addToPlaylist(it[position].audio.id, pid)
            }
        }
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlists = managePlaylists.getAllPlaylists()
        }
    }
}