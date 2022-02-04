package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.music_service.service.utils.LOCAL_ROOT_ID
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LocalAudioViewModel(
    private val retrieveLocalAudio: RetrieveLocalAudio,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists,
    private val mediaServiceControl: MediaServiceControl,
) : ViewModel(), ChildLoadedListener, StartChildLoadingListener {
    val localAudioData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = listOf()
    var dynamicallyAddAudioPosition: Int = 0

    init {
//        viewModelScope.launch {
//            retrieveMusic()
//        }
        viewModelScope.launch {
            managePlaylists.getAllPlaylists().collect {
                playlists = it
            }
        }

        // new
        mediaServiceControl.setStartChildLoadingListener(this)
        mediaServiceControl.setChildLoadedListener(this)
        mediaServiceControl.subscribe(LOCAL_ROOT_ID)
        //
    }

    // new
    override fun onStartChildLoading(audios: Resource<List<AudioWrapper>>) {
        localAudioData.postValue(audios.data)
    }

    override fun onChildLoaded(audios: Resource<List<AudioWrapper>>) {
        viewModelScope.launch {
            audios.data?.map {
                it.isFavourite = manageFavourite.hasAudio(it.audio.id)
                it.isRecent = manageRecent.hasAudio(it.audio.id)
            }
            localAudioData.postValue(audios.data)
        }
    }

    override fun onCleared() {
        mediaServiceControl.unsubscribe()
        super.onCleared()
    }
    //

    private suspend fun retrieveMusic() {
        val list = retrieveLocalAudio.retrieveLocalMusic().toMutableList()
        list.map {
            it.isFavourite = manageFavourite.hasAudio(it.audio.id)
            it.isRecent = manageRecent.hasAudio(it.audio.id)
        }
        localAudioData.value = list
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            localAudioData.value?.let {
                if (manageFavourite.hasAudio(it[position].audio.id)) {
                    manageFavourite.removeFavourite(it[position].audio.id)
                } else {
                    manageFavourite.addFavourite(it[position].audio.id)
                }
            }
            retrieveMusic()
        }
    }

    fun removeFromRecent(position: Int) {
        viewModelScope.launch {
            localAudioData.value?.let {
                manageRecent.removeAudio(it[position].audio.id)
                retrieveMusic()
            }
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = localAudioData.value?.let {
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
                localAudioData.value?.let {
                    if (!manageFavourite.hasAudio(it[position].audio.id)) {
                        val list = localAudioData.value?.toMutableList()
                        manageFavourite.addFavourite(it[position].audio.id)
                        list?.set(position, list[position].copy(isFavourite = true))
                        localAudioData.value = list
                    }
                }
            }
            localAudioData.value?.let {
                managePlaylists.addToPlaylist(it[position].audio.id, pid)
            }
        }
    }



}