package com.automotive.bootcamp.mediaplayer.domain.useCases

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.viewModels.ChildLoadedListener
import com.automotive.bootcamp.mediaplayer.viewModels.StartChildLoadingListener
import com.automotive.bootcamp.mediaplayer.presentation.extensions.mapToAudioWrapper

class MediaServiceControl(
//    private val musicServiceConnection: MusicServiceConnection
    ) {
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
//        musicServiceConnection.subscribe(mediaRootId, object : MediaBrowserCompat.SubscriptionCallback() {
//                override fun onChildrenLoaded(
//                    parentId: String,
//                    children: MutableList<MediaBrowserCompat.MediaItem>,
//                    bundle: Bundle
//                ) {
//                    super.onChildrenLoaded(parentId, children)
//                    val audios = children.map {
//                           it.mapToAudioWrapper()
//                    }
//                    childLoadedListener?.onChildLoaded(Resource.success(audios))
//                }
//            })
    }

    fun unsubscribe() {
        mediaRootId?.let {
//            musicServiceConnection.unsubscribe(it, object : MediaBrowserCompat.SubscriptionCallback() {})
        }
    }
}