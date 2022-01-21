package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.base.CoroutineViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.domain.models.SongWrapper
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic
import kotlinx.coroutines.launch

class LocalMusicViewModel(private val getLocalMusic: GetLocalMusic) : CoroutineViewModel() {

    val localMusicData by lazy { MutableLiveData<List<SongWrapper>>() }

    init {
        viewModelScope.launch {
            val songs = getLocalMusic.getLocalSongs()
            localMusicData.value = songs.map { songItem ->
                SongWrapper(
                    song = Song(
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
}