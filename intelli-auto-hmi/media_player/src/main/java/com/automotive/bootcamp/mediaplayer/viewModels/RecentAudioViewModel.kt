package com.automotive.bootcamp.mediaplayer.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class RecentAudioViewModel(
    private val retrieveRecentAudio: RetrieveRecentAudio,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {

    val recentMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlists: List<PlaylistWrapper>? = null
    var dynamicallyAddAudioPosition: Int = 0

    init {
        viewModelScope.launch {
            retrieveMusic()
            getAllPlaylists()
        }
    }

    private suspend fun retrieveMusic() {
        val list = retrieveRecentAudio.retrieveRecentAudio()?.toMutableList()
        list?.map {
            it.isFavourite = manageFavourite.hasAudio(it.audio.id)
            it.isRecent = true
        }
        recentMusicData.value = list
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            recentMusicData.value?.let {
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
            recentMusicData.value?.let{
                manageRecent.removeAudio(it[position].audio.id)
                retrieveMusic()
            }
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = recentMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(1, "name", it).mapToPlaylistWrapper() }
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
                recentMusicData.value?.let {
                    if (!manageFavourite.hasAudio(it[position].audio.id)) {
                        val list = recentMusicData.value?.toMutableList()
                        manageFavourite.addFavourite(it[position].audio.id)
                        list?.set(position, list[position].copy(isFavourite = true))
                        recentMusicData.value = list
                    }
                }
            }
            recentMusicData.value?.let {
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