package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.base.CoroutineViewModel
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapPlaylist
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalMusic
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class LocalMusicViewModel(
    private val retrieveLocalMusic: RetrieveLocalMusic,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val addRemoveRecent: AddRemoveRecent
) : CoroutineViewModel() {

    val localMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }

    init {
        viewModelScope.launch {
            val audioList = retrieveLocalMusic.retrieveLocalMusic()
            localMusicData.value = audioList.map { audio ->
                audio.mapToAudio().wrapAudio()
            }
        }
    }

    fun setIsFavourite(position: Int) {
        localMusicData.value =
            addRemoveFavourite.addRemoveFavourite(localMusicData.value, position)
    }

    fun setIsRecent(position: Int) {
        localMusicData.value =
            addRemoveRecent.addRemoveRecent(localMusicData.value, position)
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = localMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(1, "name", it).wrapPlaylist() }
    }
}