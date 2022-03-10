package com.automotive.bootcamp.mediaplayer.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.automotive.bootcamp.common.utils.Event
import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.presentation.MediaPlayerSettingsFragment

class MusicServiceConnection(
    private val context: Context
) {
    init {
        Log.d("listTAG", "init MusicServiceConnection")
    }
    private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()
    val isConnected: LiveData<Event<Resource<Boolean>>> = _isConnected

    private val _networkError = MutableLiveData<Event<Resource<Boolean>>>()
    val networkError: LiveData<Event<Resource<Boolean>>> = _networkError

    private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
    val playbackState: LiveData<PlaybackStateCompat?> = _playbackState

    private val _curPlayingSong = MutableLiveData<MediaMetadataCompat?>()
    val curPlayingSong: LiveData<MediaMetadataCompat?> = _curPlayingSong

    private val sessionToken = MediaPlayerSettingsFragment.selectedServiceToken

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser by lazy {
        Log.d("listTAG", "connect to browser")
        MediaBrowserCompat(
            context,
            MediaPlayerSettingsFragment.selectedService?.componentName,
            mediaBrowserConnectionCallback,
            null
        ).apply { connect() }
    }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }
    lateinit var mediaController: MediaControllerCompat
    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            Log.d("listTAG", "-------------------> connected")
            mediaController =
                MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                    registerCallback(MediaControllerCallback())
                }
//                val mediaController = MediaControllerCompat(context, sessionToken).apply {
//                    registerCallback(MediaControllerCallback())
//                }
                _isConnected.postValue(Event(Resource.success(true)))
                buildTransportControls()
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(
                Event(
                    Resource.error(
                        "The connection was suspended", false
                    )
                )
            )
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(
                Event(
                    Resource.error(
                        "Couldn't connect to media browser", false
                    )
                )
            )
        }
    }

    fun buildTransportControls() {
        val metadata = mediaController.metadata
        val pbState = mediaController.playbackState

        // Register a Callback to stay in sync
        mediaController.registerCallback(object : MediaControllerCompat.Callback() {

            override fun onMetadataChanged(metadata: MediaMetadataCompat?) {}

            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {}
        })
    }

    fun play() {
        mediaController?.transportControls?.play()
    }

    fun pause() {
        MediaControllerCompat.getMediaController(context as Activity).transportControls.pause()
    }

    fun skipToNext() {
        MediaControllerCompat.getMediaController(context as Activity).transportControls.skipToNext()
    }

    fun skipToPrevious() {
        MediaControllerCompat.getMediaController(context as Activity).transportControls.skipToPrevious()
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.postValue(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _curPlayingSong.postValue(metadata)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_ERROR -> _networkError.postValue(
                    Event(
                        Resource.error(
                            "Couldn't connect to the server. Please check your internet connection.",
                            null
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
