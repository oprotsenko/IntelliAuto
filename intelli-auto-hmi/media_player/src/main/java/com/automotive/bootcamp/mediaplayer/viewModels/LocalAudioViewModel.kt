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
//    private val mediaServiceControl: MediaServiceControl,
) : ViewModel()
//    , ChildLoadedListener, StartChildLoadingListener
{

    val localAudioData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = listOf()
    var dynamicallyAddAudioPosition: Int = 0

    init {
        viewModelScope.launch {
            retrieveMusic()
            managePlaylists.getAllPlaylists().collect { list ->
                playlists = list?.map { playlist ->
                    playlist.mapToPlaylistWrapper()
                }
            }
        }

        // new
//        mediaServiceControl.setStartChildLoadingListener(this)
//        mediaServiceControl.setChildLoadedListener(this)
//        mediaServiceControl.subscribe(LOCAL_ROOT_ID)
        //
    }

    // new
//    override fun onStartChildLoading(audios: Resource<List<AudioWrapper>>) {
//        localAudioData.postValue(audios.data)
//    }
//
//    override fun onChildLoaded(audios: Resource<List<AudioWrapper>>) {
//        viewModelScope.launch {
//            audios.data?.map {
//                it.isFavourite = manageFavourite.hasAudio(it.audio.id)
//                it.isRecent = manageRecent.hasAudio(it.audio.id)
//            }
//
//            localAudioData.postValue(audios.data)
//        }
//    }
//
//    override fun onCleared() {
//        mediaServiceControl.unsubscribe()
//        super.onCleared()
//    }


    suspend fun retrieveMusic() {
        val list = retrieveLocalAudio.retrieveLocalMusic().toMutableList()
        localAudioData.postValue(list.map { audio ->
            audio.wrapAudio(
                isFavourite = manageFavourite.hasAudio(audio.id),
                isRecent = manageRecent.hasAudio(audio.id)
            )
        })
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            localAudioData.value?.let {
                val aid = it[position].audio.id
                if (manageFavourite.hasAudio(aid)) {
                    manageFavourite.removeFavourite(aid)
                } else {
                    manageFavourite.addFavourite(aid)
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
        return list?.let { Playlist(name = "name", list = it).mapToPlaylistWrapper() }
    }

    fun createPlaylist(playlistName: String, position: Int) {
        viewModelScope.launch {
            addToPlaylist(managePlaylists.createPlaylist(playlistName), position)
        }
    }

    fun addToPlaylist(pid: Long, position: Int) {
        localAudioData.value?.let {
            val aid = it[position].audio.id
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
}