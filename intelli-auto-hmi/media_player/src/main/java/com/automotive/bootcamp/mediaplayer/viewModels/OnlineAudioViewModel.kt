package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnlineAudioViewModel(
    private val retrieveOnlineAudio: RetrieveOnlineAudio,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {
    val onlineAudioData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = listOf()
    var dynamicallyAddAudioId: Long = 0

    init {
        viewModelScope.apply {
            launch {
                retrieveAudio()
                managePlaylists.getAllPlaylists().collect { list ->
                    playlists = list?.map { playlist ->
                        playlist.mapToPlaylistWrapper()
                    }
                }
            }
        }
    }

    suspend fun retrieveAudio() {
        val list = retrieveOnlineAudio.retrieveOnlineMusic()?.toMutableList()
        onlineAudioData.postValue(list?.map { audio ->
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
            retrieveAudio()
        }
    }

    fun removeFromRecent(aid: Long) {
        viewModelScope.launch {
            manageRecent.removeAudio(aid)
            retrieveAudio()
        }
    }

    fun isRecent(aid: Long): Boolean? {
        return onlineAudioData.value?.first { it.audio.id == aid }?.isRecent
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = onlineAudioData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(name = "online", list = it).mapToPlaylistWrapper() }
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
