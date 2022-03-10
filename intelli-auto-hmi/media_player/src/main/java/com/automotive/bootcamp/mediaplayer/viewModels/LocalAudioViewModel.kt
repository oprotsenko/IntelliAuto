package com.automotive.bootcamp.mediaplayer.viewModels

import android.app.Application
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalAudio
import com.automotive.bootcamp.mediaplayer.presentation.MediaPlayerSettingsFragment
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToAudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.LOCAL_PLAYLIST
import com.automotive.bootcamp.mediaplayer.utils.MusicServiceConnection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LocalAudioViewModel(
    private val retrieveLocalAudio: RetrieveLocalAudio,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists,
    application: Application
) : AndroidViewModel(application) {
    val localAudioData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = listOf()
    var dynamicallyAddAudioId: Long = 0

    val musicServiceConnection by lazy { MusicServiceConnection(application) }

    init {
        if (MediaPlayerSettingsFragment.selectedService == null) {
            viewModelScope.launch {
                retrieveMusic()
                managePlaylists.getAllPlaylists().collect { list ->
                    playlists = list?.map { playlist ->
                        playlist.mapToPlaylistWrapper()
                    }
                }
            }
        } else {
            connectToService()
        }
    }

    fun connectToService() {
        musicServiceConnection.subscribe(
            LOCAL_PLAYLIST,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map {
                        it.mapToAudioWrapper()
                    }
                    localAudioData.postValue(items)
                }
            })
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
        return list?.let { Playlist(name = LOCAL_PLAYLIST, list = it).mapToPlaylistWrapper() }
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