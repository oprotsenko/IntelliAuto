package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageRecent
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CustomPlaylistViewModel(
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {
    var customAudioFlow: Flow<List<AudioWrapper>?>? = null
    var customAudio: List<AudioWrapper>? = listOf()
    var playlists: List<PlaylistWrapper>? = listOf()

    var dynamicallyAddAudioId: Long = 0
    private var pid: Long? = null

    init {
        viewModelScope.launch {
            managePlaylists.getAllPlaylists().collect { list ->
                playlists = list?.map { playlist ->
                    playlist.mapToPlaylistWrapper()
                }
            }
        }
    }

    fun init(playlist: PlaylistWrapper?) {
        pid = playlist?.playlist?.id
        pid?.let {
            customAudioFlow = managePlaylists.getPlaylistAudio(it).map { list ->
                list?.map { audio ->
                    audio.wrapAudio()
                }
            }
        }
    }

    suspend fun updateAudioList(audio: List<AudioWrapper>?) {
        customAudio = audio
        customAudio?.map {
            it.isFavourite = manageFavourite.hasAudio(it.audio.id)
            it.isRecent = true
        }
    }

    fun removeFromCurrentPlaylist(aid: Long) {
        viewModelScope.launch {
            pid?.let { pid ->
                managePlaylists.removeFromPlaylist(aid, pid)
            }
        }
    }

    fun setIsFavourite(aid: Long) {
        viewModelScope.launch {
            if (manageFavourite.hasAudio(aid)) {
                manageFavourite.removeFavourite(aid)
            } else {
                manageFavourite.addFavourite(aid)
            }
        }
    }

    fun removeFromRecent(aid: Long) {
        viewModelScope.launch {
            manageRecent.removeAudio(aid)
        }
    }

    fun isRecent(aid: Long): Boolean? {
        return customAudio?.first { it.audio.id == aid }?.isRecent
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = customAudio?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(name = "custom", list = it).mapToPlaylistWrapper() }
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