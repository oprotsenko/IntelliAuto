package com.automotive.bootcamp.music_service.service

import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.util.Log
import com.automotive.bootcamp.music_service.data.ServiceSources
import com.automotive.bootcamp.music_service.utils.*
import com.automotive.bootcamp.music_service.utils.ALBUMS_ROOT_ID

class BrowseTree(
    musicSource: ServiceSources
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    init {
        Log.d("serviceTAG", "source items " + musicSource.sourceList.size)
        musicSource.forEach { mediaItem ->
            val albumMediaId = mediaItem.getString(METADATA_KEY_ALBUM)
            val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)
            mediaIdToChildren.forEach {
                Log.d("serviceTAGS", "root -----> " + it.key)
            }
            albumChildren += mediaItem
        }

//        val albumsMetadata = Builder().apply {
//            putString(METADATA_KEY_MEDIA_ID, ALBUMS_ROOT_ID)
//            putString(METADATA_KEY_TITLE, ALBUMS_ROOT_ID)
//            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
//        }.build()
//
//        val recentMetadata = Builder().apply {
//            putString(METADATA_KEY_MEDIA_ID, RECENT_ROOT_ID)
//            putString(METADATA_KEY_TITLE, RECENT_ROOT_ID)
//            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
//        }.build()
//
//        val favouriteMetadata = Builder().apply {
//            putString(METADATA_KEY_MEDIA_ID, FAVOURITE_ROOT_ID)
//            putString(METADATA_KEY_TITLE, FAVOURITE_ROOT_ID)
//            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
//        }.build()
//
//        val rootList = mediaIdToChildren[BROWSABLE_ROOT_ID] ?: mutableListOf()
//        rootList += recentMetadata
//        rootList += favouriteMetadata
//        mediaIdToChildren[BROWSABLE_ROOT_ID] = rootList
//
//        val albums = mediaIdToChildren[ALBUMS_ROOT_ID] ?: mutableListOf()
//        albums += recentMetadata
//        albums += favouriteMetadata
//        mediaIdToChildren[ALBUMS_ROOT_ID] = albums
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    fun addMetadata(mediaId: String, children: MediaMetadataCompat) {
        val newChildren = mediaIdToChildren[mediaId] ?: mutableListOf()
        newChildren += children
        mediaIdToChildren[mediaId] = newChildren
    }

    private fun buildAlbumRoot(mediaItem: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val albumMetadata = Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, mediaItem.getString(METADATA_KEY_ALBUM))
            putString(
                METADATA_KEY_TITLE,
                mediaItem.getString(METADATA_KEY_ALBUM)
            )
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val rootList = mediaIdToChildren[BROWSABLE_ROOT_ID] ?: mutableListOf()
        rootList += albumMetadata
        mediaIdToChildren[BROWSABLE_ROOT_ID] = rootList

        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[albumMetadata.getString(METADATA_KEY_MEDIA_ID)] = it
        }
    }
}