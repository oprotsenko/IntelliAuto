package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.base.CoroutineViewModel
import com.automotive.bootcamp.common.utils.RECENT_PLAYLIST_ID
import com.automotive.bootcamp.common.utils.RECENT_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapPlaylist
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveFavourite
import com.automotive.bootcamp.mediaplayer.domain.useCases.AddRemoveRecent
import com.automotive.bootcamp.mediaplayer.domain.useCases.RetrieveRecentAudio
import com.automotive.bootcamp.mediaplayer.presentation.extensions.unwrap
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import kotlinx.coroutines.launch

class RecentAudioViewModel(
    private val retrieveRecentAudio: RetrieveRecentAudio,
    private val addRemoveFavourite: AddRemoveFavourite,
    private val addRemoveRecent: AddRemoveRecent
) : CoroutineViewModel() {
    val recentAudioData by lazy { MutableLiveData<List<AudioWrapper>>() }

    init {
        viewModelScope.launch {
            val audioList = retrieveRecentAudio.retrieveRecentAudio()

            if (audioList != null) {
                recentAudioData.value = audioList.map { audio ->
                    audio.mapToAudio().wrapAudio()
                }
            }
        }
    }

    fun setIsFavourite(position: Int) {
        viewModelScope.launch {
            recentAudioData.value =
                addRemoveFavourite.addRemoveFavourite(recentAudioData.value, position)
        }
    }

    fun setIsRecent(position: Int) {
        viewModelScope.launch {
            recentAudioData.value =
                addRemoveRecent.addRemoveRecent(recentAudioData.value, position)
        }
    }

    fun getAudioList(): PlaylistWrapper? {
        val list = recentAudioData.value?.let {
            it.map { wrapper ->
                wrapper.unwrap()
            }
        }
        return list?.let { Playlist(RECENT_PLAYLIST_ID, RECENT_PLAYLIST_NAME, it).wrapPlaylist() }
    }
}