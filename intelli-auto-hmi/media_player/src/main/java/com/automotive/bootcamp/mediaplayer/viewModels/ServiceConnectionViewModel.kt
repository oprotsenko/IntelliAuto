package com.automotive.bootcamp.mediaplayer.viewModels

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToAudioWrapper
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.utils.LOCAL_PLAYLIST
import com.automotive.bootcamp.mediaplayer.utils.MusicServiceConnection

class ServiceConnectionViewModel(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val _mediaItems = MutableLiveData<Resource<List<AudioWrapper>>>()
    val mediaItems: LiveData<Resource<List<AudioWrapper>>> = _mediaItems

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val curPlayingAudio = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState

    init {
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(LOCAL_PLAYLIST ,object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    it.mapToAudioWrapper()
                }
                _mediaItems.postValue(Resource.success(items))
            }
        })
    }
}