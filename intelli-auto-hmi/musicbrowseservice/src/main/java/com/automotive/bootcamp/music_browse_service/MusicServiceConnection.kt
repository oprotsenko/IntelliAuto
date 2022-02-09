package com.automotive.bootcamp.music_browse_service

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.automotive.bootcamp.music_browse_service.utils.Event
import com.automotive.bootcamp.music_browse_service.utils.NETWORK_ERROR
import com.automotive.bootcamp.music_browse_service.utils.Resource

class MusicServiceConnection(
    context: Context
) {
    private val _isConnected by lazy { MutableLiveData<Event<Resource<Boolean>>>() }
    val isConnected: LiveData<Event<Resource<Boolean>>> = _isConnected

    private val _networkError by lazy { MutableLiveData<Event<Resource<Boolean>>>() }
    val networkError: LiveData<Event<Resource<Boolean>>> = _networkError

    private val _playbackState by lazy { MutableLiveData<PlaybackStateCompat?>() }
    val playbackState: LiveData<PlaybackStateCompat?> = _playbackState

    private val _currentPlayingAudio by lazy { MutableLiveData<MediaMetadataCompat?>() }
    val currentPlayingAudio: LiveData<MediaMetadataCompat?> = _currentPlayingAudio

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            MyMusicService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    lateinit var mediaController: MediaControllerCompat

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }


    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            _isConnected.postValue(Event(Resource.success(true)))
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(Event(Resource.error("the connection was suspended", false)))
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(
                Event(
                    Resource.error(
                        "couldn't connect to media browser",
                        false
                    )
                )
            )
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.postValue(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _currentPlayingAudio.postValue(metadata)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            when (event) {
                NETWORK_ERROR -> _networkError.postValue(
                    Event(
                        Resource.error(
                            "couldn't connect to the server", null
                        )
                    )
                )
            }
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}