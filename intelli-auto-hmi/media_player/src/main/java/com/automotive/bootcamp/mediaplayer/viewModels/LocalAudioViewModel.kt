package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalAudio
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LocalAudioViewModel(
    private val retrieveLocalAudio: RetrieveLocalAudio,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists,
) : ViewModel() {
    val localAudioData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = listOf()
    var dynamicallyAddAudioId: Long = 0

    init {
        viewModelScope.launch {
            retrieveMusic()
            managePlaylists.getAllPlaylists().collect { list ->
                playlists = list?.map { playlist ->
                    playlist.mapToPlaylistWrapper()
                }
            }
        }
    }

    suspend fun retrieveMusic() {
        val list = retrieveLocalAudio.retrieveLocalMusic().toMutableList()
        localAudioData.postValue(list.map { audio ->
            audio.wrapAudio(
                isFavourite = manageFavourite.hasAudio(audio.id),
                isRecent = manageRecent.hasAudio(audio.id)
            )
        })
    }

    fun setIsFavourite(aid: Long) {
        viewModelScope.launch {
            if (manageFavourite.hasAudio(aid)) {
                manageFavourite.removeFavourite(aid)
            } else {
                manageFavourite.addFavourite(aid)
            }
            retrieveMusic()
        }
    }

    fun removeFromRecent(aid: Long) {
        viewModelScope.launch {
            manageRecent.removeAudio(aid)
            retrieveMusic()
        }
    }

    fun isRecent(aid: Long): Boolean? {
        return localAudioData.value?.first { it.audio.id == aid }?.isRecent
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = localAudioData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(name = "local", list = it).mapToPlaylistWrapper() }
    }

    fun createPlaylist(playlistName: String, aid: Long) {
        viewModelScope.launch {
            addToPlaylist(managePlaylists.createPlaylist(playlistName), aid)
        }
    }

    fun addToPlaylist(pid: Long, aid: Long) {
        viewModelScope.launch {
            if (pid == manageFavourite.getId()) {
                if (!manageFavourite.hasAudio(aid)) {
                    manageFavourite.addFavourite(aid)
                }
            } else {
                managePlaylists.addToPlaylist(aid, pid)
            }
        }
    }
}