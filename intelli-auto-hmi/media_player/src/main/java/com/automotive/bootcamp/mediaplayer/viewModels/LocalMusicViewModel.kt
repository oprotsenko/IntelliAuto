package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.PlaylistRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class LocalMusicViewModel(
    private val retrieveLocalMusic: RetrieveLocalMusic,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val addRemoveRecent: AddRemoveRecent,
    private val addToPlaylist: AddToPlaylist,
    private val createPlaylist: CreatePlaylist,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    val localMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }
    var playlistsData: List<PlaylistWrapper>? = null

    init {
        viewModelScope.launch {
            localMusicData.value = retrieveLocalMusic.retrieveLocalMusic().map { audio ->
                audio.mapToAudio().wrapAudio()
            }
            val list = localMusicData.value?.toMutableList()
            list?.map {
                it.isFavourite = addRemoveFavourite.hasAudio(it.audio.id)
            }
            localMusicData.value = list
            getAllPlaylists()
        }
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            val list = localMusicData.value?.toMutableList()
            list?.let {
                if (addRemoveFavourite.hasAudio(it[position].audio.id)) {
                    addRemoveFavourite.removeFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = false)
                } else {
                    addRemoveFavourite.addFavourite(it[position].audio.id)
                    list[position] = list[position].copy(isFavourite = true)
                }
            }
            localMusicData.value = list
        }
    }

    fun setIsRecent(position: Int) {
        viewModelScope.launch {
            localMusicData.value =
                addRemoveRecent.addRemoveRecent(localMusicData.value, position)
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = localMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(1, "name", it).mapToPlaylistWrapper() }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch {
            createPlaylist.createPlaylist(playlistName)
        }
    }

    fun addToPlaylist(pid: Long, position: Int) {
        viewModelScope.launch {
            if (playlistRepository.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.id == pid) {
                localMusicData.value?.let {
                    if (!addRemoveFavourite.hasAudio(it[position].audio.id)) {
                        val list = localMusicData.value?.toMutableList()
                        addRemoveFavourite.addFavourite(it[position].audio.id)
                        list?.set(position, list[position].copy(isFavourite = true))
                        localMusicData.value = list
                    }
                }
            }
            localMusicData.value?.let {
                addToPlaylist.addToPlaylist(it[position].audio.id, pid)
            }
        }
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlistsData = playlistRepository.getAllPlaylists()
        }
    }
}