package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageRecent
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CustomPlaylistViewModel(
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {
    var customAudioFlow: Flow<List<AudioWrapper>?>? = null
    var customAudio: List<AudioWrapper>? = listOf()
    var playlists: List<PlaylistWrapper>? = listOf()

    var dynamicallyAddAudioPosition: Int = 0
    private var pid: Long? = null

    init {
        viewModelScope.launch {
            managePlaylists.getAllPlaylists().collect {
                playlists = it
            }
        }
    }

    fun init(playlist: PlaylistWrapper?) {
        pid = playlist?.playlist?.id
        pid?.let {
            customAudioFlow = managePlaylists.getPlaylistAudio(it)
        }
    }

    suspend fun updateAudioList(audio:List<AudioWrapper>?) {
        customAudio = audio
        customAudio?.map {
            it.isFavourite = manageFavourite.hasAudio(it.audio.id)
            it.isRecent = true
        }
    }

    fun removeFromCurrentPlaylist(position: Int) {
        viewModelScope.launch {
            customAudio?.let {
                pid?.let { pid ->
                    managePlaylists.removeFromPlaylist(it[position].audio.id, pid)
                }
            }
        }
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            val list = customAudio?.toMutableList()
            list?.let {
                val aid = it[position].audio.id
                if (manageFavourite.hasAudio(aid)) {
                    manageFavourite.removeFavourite(aid)
                } else {
                    manageFavourite.addFavourite(aid)
                }
            }
        }
    }

    fun removeFromRecent(position: Int) {
        viewModelScope.launch {
            customAudio?.let {
                manageRecent.removeAudio(it[position].audio.id)
            }
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = customAudio?.let {
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
        customAudio?.let {
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