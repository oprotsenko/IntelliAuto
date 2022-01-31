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
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalAudio
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnlineAudioViewModel(
    private val retrieveLocalAudio: RetrieveLocalAudio,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {
    val onlineAudioData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = listOf()
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

        viewModelScope.launch {
            managePlaylists.getAllPlaylists().collect {
                playlists = it
            }
        }
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            val list = onlineAudioData.value?.toMutableList()
            list?.let {
                if (manageFavourite.hasAudio(it[position].audio.id)) {
                    manageFavourite.removeFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = false)
                } else {
                    manageFavourite.addFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = true)
                }
            }
            onlineAudioData.value = list
        }
    }

    fun removeFromRecent(position: Int) {
        viewModelScope.launch {
            onlineAudioData.value?.let {
                manageRecent.removeAudio(it[position].audio.id)
            }
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = onlineAudioData.value?.let {
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
            if (pid == manageFavourite.getId()) {
                onlineAudioData.value?.let {
                    if (!manageFavourite.hasAudio(it[position].audio.id)) {
                        val list = onlineAudioData.value?.toMutableList()
                        manageFavourite.addFavourite(it[position].audio.id)
                        list?.set(position, list[position].copy(isFavourite = true))
                        onlineAudioData.value = list
                    }
                }
            }
            onlineAudioData.value?.let {
                managePlaylists.addToPlaylist(it[position].audio.id, pid)
            }
        }
    }
}