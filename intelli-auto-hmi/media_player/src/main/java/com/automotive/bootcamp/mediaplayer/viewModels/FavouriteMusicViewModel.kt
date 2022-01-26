package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.FavouriteAudioRepository
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.*
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class FavouriteMusicViewModel(
    private val retrieveFavouriteAudioRepository: FavouriteAudioRepository,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val removeRecent: RemoveRecent,
    private val addToPlaylist: AddToPlaylist,
    private val createPlaylist: CreatePlaylist,
) : ViewModel() {

    val favouriteMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }

    init {
        viewModelScope.launch {
            val audioList = retrieveFavouriteAudioRepository.getPlaylist()?.list
            favouriteMusicData.value = audioList?.map { playlistItem ->
                playlistItem.mapToAudio().wrapAudio()
            }
        }
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            favouriteMusicData.value =
                addRemoveFavourite.addRemoveFavourite(favouriteMusicData.value, position)
        }
    }

    fun removeFromRecent(position: Int) {
        viewModelScope.launch {
            favouriteMusicData.value =
                removeRecent.execute(favouriteMusicData.value, position)
        }
    }

    fun getAudioList(): PlaylistWrapper {
        val list = Playlist(1, FAVOURITE_PLAYLIST_NAME, favouriteMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        })
        return PlaylistWrapper(list.name, list)
    }
}