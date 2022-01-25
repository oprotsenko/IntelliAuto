package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.data.models.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.models.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.models.wrapPlaylist
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalMusic
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.unwrap
import kotlinx.coroutines.launch

class FavouriteMusicViewModel(
    private val retrieveLocalMusic: RetrieveLocalMusic,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val addRemoveRecent: AddRemoveRecent
) : ViewModel() {

    val favouriteMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }

    init {
        viewModelScope.launch {
            val audioList = retrieveLocalMusic.retrieveLocalMusic()
            favouriteMusicData.value = audioList.map { audio ->
                audio.mapToAudio().wrapAudio()
            }
        }
    }

    fun setIsFavourite(position: Int) {
        favouriteMusicData.value =
            addRemoveFavourite.addRemoveFavourite(favouriteMusicData.value, position)
    }

    fun setIsRecent(position: Int) {
        favouriteMusicData.value =
            addRemoveRecent.addRemoveRecent(favouriteMusicData.value, position)
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = favouriteMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist("id", it).wrapPlaylist() }
    }
}