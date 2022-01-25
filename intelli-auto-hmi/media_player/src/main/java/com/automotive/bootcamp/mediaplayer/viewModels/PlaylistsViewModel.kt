package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.data.StorageAudioRepository
import com.automotive.bootcamp.mediaplayer.data.storage.StorageMedia
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.relations.PlaylistWithAudios
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.relations.mapToPlaylistWrapper
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveLocalMusic
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val storageAudioRepository: StorageMedia,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val addRemoveRecent: AddRemoveRecent
) : ViewModel() {

    val localMusicData by lazy { MutableLiveData<List<PlaylistWrapper>>() }

    init {
        viewModelScope.launch {
            val list = storageAudioRepository.getAllPlaylistsWithAudios()
            val lastItemList = list.toMutableList()
            lastItemList.add(PlaylistWithAudios(PlaylistEntity(0, "", ""), listOf()))
            localMusicData.value = lastItemList.map {
                it.mapToPlaylistWrapper()
            }
        }
    }
}