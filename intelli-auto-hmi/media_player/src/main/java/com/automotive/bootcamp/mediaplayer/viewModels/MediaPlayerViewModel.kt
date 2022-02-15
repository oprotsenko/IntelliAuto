package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.mediaplayer.domain.useCases.InitializeMediaPlayer
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MediaPlayerViewModel : ViewModel(), KoinComponent {
    private val initializeMediaPlayer: InitializeMediaPlayer by inject()
    private var _query: MutableLiveData<String?> = MutableLiveData<String?>()

    init {
        viewModelScope.launch {
            initializeMediaPlayer.execute()
        }
    }

    val searchQuery: LiveData<String?>
        get() = _query

    fun setQuery(queryData: String?) {
        _query.value = queryData
    }
}