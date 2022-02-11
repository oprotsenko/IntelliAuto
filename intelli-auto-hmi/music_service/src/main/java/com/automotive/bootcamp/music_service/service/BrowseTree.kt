package com.automotive.bootcamp.music_service.service

import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.util.Log
import com.automotive.bootcamp.music_service.data.ServiceSources
import com.automotive.bootcamp.music_service.utils.BROWSABLE_ROOT_ID
import com.automotive.bootcamp.music_service.utils.METADATA_KEY_FLAGS

class BrowseTree(
    musicSource: ServiceSources
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    init {
        Log.d("serviceTAG", "init tree")
        val rootList = mediaIdToChildren[BROWSABLE_ROOT_ID] ?: mutableListOf()
        mediaIdToChildren[BROWSABLE_ROOT_ID] = rootList

        musicSource.forEach { mediaItem ->
            val albumMediaId = mediaItem.getString(METADATA_KEY_ALBUM)
            val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)

            albumChildren += mediaItem
            Log.d("serviceTAG", "album amount " + mediaIdToChildren[albumMediaId]?.size)
            Log.d("serviceTAG", "album " + albumMediaId + " + " + mediaItem.getString(METADATA_KEY_TITLE))
            Log.d("serviceTAG", "mediaIdToChildren[" + albumMediaId + "] " + mediaIdToChildren[albumMediaId])
        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

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