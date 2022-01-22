package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.base.CoroutineViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import kotlinx.coroutines.launch

class LocalMusicViewModel(
    private val getLocalMusic: GetLocalMusic,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val addRemoveRecent: AddRemoveRecent
) : CoroutineViewModel() {

    val localMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }

    init {
        viewModelScope.launch {
            val songs = getLocalMusic.getLocalSongs()
            localMusicData.value = songs.map { songItem ->
                AudioWrapper(
                    audio = Audio(
                        id = songItem.id,
                        cover = songItem.cover,
                        title = songItem.title,
                        artist = songItem.artist,
                        duration = songItem.duration,
                        songURL = songItem.url
                    )
                )
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
}