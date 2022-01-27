package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalMusic
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class OnlineMusicViewModel(
    private val retrieveLocalMusic: RetrieveLocalMusic,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {

    val onlineMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = null
    var dynamicallyAddAudioPosition: Int = 0

    init {
//        viewModelScope.launch {
//            val list = retrieveLocalMusic.retrieveLocalMusic().map { audio ->
//                audio.mapToAudio().wrapAudio()
//            }.toMutableList()
//            list.map {
//                it.isFavourite = addRemoveFavourite.hasAudio(it.audio.id)
//            }
//            onlineMusicData.value = list
//            getAllPlaylists()
//        }
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            val list = onlineMusicData.value?.toMutableList()
            list?.let {
                if (manageFavourite.hasAudio(it[position].audio.id)) {
                    manageFavourite.removeFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = false)
                } else {
                    manageFavourite.addFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = true)
                }
            }
            onlineMusicData.value = list
        }
    }

    fun removeFromRecent(position: Int) {
        viewModelScope.launch {
            onlineMusicData.value?.let {
                manageRecent.removeAudio(it[position].audio.id)
            }
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = onlineMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(1, "name", it).mapToPlaylistWrapper() }
    }

    fun createPlaylist(playlistName: String, position: Int) {
        viewModelScope.launch {
            addToPlaylist(managePlaylists.createPlaylist(playlistName), position)
        }
    }

    fun addToPlaylist(pid: Long, position: Int) {
        viewModelScope.launch {
            if (managePlaylists.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.id == pid) {
                onlineMusicData.value?.let {
                    if (!manageFavourite.hasAudio(it[position].audio.id)) {
                        val list = onlineMusicData.value?.toMutableList()
                        manageFavourite.addFavourite(it[position].audio.id)
                        list?.set(position, list[position].copy(isFavourite = true))
                        onlineMusicData.value = list
                    }
                }
            }
            onlineMusicData.value?.let {
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