package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalMusic
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper

class OnlineMusicViewModel(
    private val retrieveLocalMusic: RetrieveLocalMusic,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val addRemoveRecent: AddRemoveRecent
) : ViewModel() {

    val onlineMusicData by lazy { MutableLiveData<List<AudioWrapper>>() }

    init {
        onlineMusicData.value = mutableListOf()
//        viewModelScope.launch {
//            val audioList = retrieveLocalMusic.retrieveLocalMusic()
//            localMusicData.value = audioList.map { audio ->
//                audio.mapToAudio().wrapAudio()
//            }
    }

    fun setIsFavourite(position: Int) {
//        localMusicData.value =
//            addRemoveFavourite.addRemoveFavourite(localMusicData.value, position)
    }

    fun setIsRecent(position: Int) {
//        localMusicData.value =
//            addRemoveRecent.addRemoveRecent(localMusicData.value, position)
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = onlineMusicData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        //todo
        return null
    }
}