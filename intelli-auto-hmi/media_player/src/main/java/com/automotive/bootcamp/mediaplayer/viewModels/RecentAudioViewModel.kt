package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManagePlaylists
import com.automotive.bootcamp.mediaplayer.domain.useCases.ManageRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveRecentAudio
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecentAudioViewModel(
    retrieveRecentAudio: RetrieveRecentAudio,
    private val manageFavourite: ManageFavourite,
    private val manageRecent: ManageRecent,
    private val managePlaylists: ManagePlaylists
) : ViewModel() {
    val recentAudioFlow: Flow<List<AudioWrapper>?> =
        retrieveRecentAudio.retrieveRecentAudio().map { list ->
            list?.map { audio ->
                audio.wrapAudio()
            }
        }

    var recentAudio: List<AudioWrapper>? = listOf()
    var playlists: List<PlaylistWrapper>? = listOf()
    var dynamicallyAddAudioId: Long = 0

    init {
        viewModelScope.launch {
            managePlaylists.getAllPlaylists().collect { list ->
                playlists = list?.map { playlist ->
                    playlist.mapToPlaylistWrapper()
                }
            }
        }
    }

    suspend fun updateAudioList(audio: List<AudioWrapper>?) {
        recentAudio = audio
        recentAudio?.map {
            it.isFavourite = manageFavourite.hasAudio(it.audio.id)
            it.isRecent = true
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
        return recentAudio?.first { it.audio.id == aid }?.isRecent
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = recentAudio?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let {
            Playlist(name = RECENT_PLAYLIST_NAME, list = it).mapToPlaylistWrapper()
        }
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