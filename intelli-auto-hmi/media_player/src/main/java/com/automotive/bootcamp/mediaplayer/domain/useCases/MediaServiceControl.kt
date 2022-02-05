package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.service.ChildLoadedListener
import com.automotive.bootcamp.mediaplayer.service.MusicServiceConnection
import com.automotive.bootcamp.mediaplayer.service.StartChildLoadingListener
import com.automotive.bootcamp.mediaplayer.service.extensions.mapToAudioWrapper

class MediaServiceControl(private val musicServiceConnection: MusicServiceConnection) {
    private var startChildLoadingListener: StartChildLoadingListener? = null
    private var childLoadedListener: ChildLoadedListener? = null
    private var mediaRootId: String? = null

    fun setStartChildLoadingListener(startChildLoadingListener: StartChildLoadingListener) {
        this.startChildLoadingListener = startChildLoadingListener
    }

    fun setChildLoadedListener(childLoadedListener: ChildLoadedListener) {
        this.childLoadedListener = childLoadedListener
    }

    fun subscribe(mediaRootId: String) {
        this.mediaRootId = mediaRootId
        startChildLoadingListener?.onStartChildLoading(Resource.loading(null))
        musicServiceConnection.subscribe(mediaRootId, object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>,
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val audios = children.map {
                           it.mapToAudioWrapper()
                    }
                    childLoadedListener?.onChildLoaded(Resource.success(audios))
                }
            })
    }

    fun unsubscribe() {
        mediaRootId?.let {
            musicServiceConnection.unsubscribe(it, object : MediaBrowserCompat.SubscriptionCallback() {})
        }
    }
}