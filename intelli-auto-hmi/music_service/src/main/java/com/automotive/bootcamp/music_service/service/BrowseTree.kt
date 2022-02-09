package com.automotive.bootcamp.music_service.service

import android.content.Context
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.util.Log
import com.automotive.bootcamp.music_service.R
import com.automotive.bootcamp.music_service.data.ServiceSources
import com.automotive.bootcamp.music_service.utils.*

class BrowseTree(
    private val context: Context,
    musicSource: ServiceSources,
    private val recentMediaId: String? = null
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    init {
        Log.d("serviceTAG", "init tree")
        val rootList = mediaIdToChildren[BROWSABLE_ROOT_ID] ?: mutableListOf()

        val localMetadata = MediaMetadataCompat.Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, LOCAL_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.local_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val remoteMetadata = MediaMetadataCompat.Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, REMOTE_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.online_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val recentMetadata = MediaMetadataCompat.Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, RECENT_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.recent_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val favouriteMetadata = MediaMetadataCompat.Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, FAVOURITE_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.favourite_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val playlistsMetadata = MediaMetadataCompat.Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, PLAYLISTS_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.playlists))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        rootList += localMetadata
        rootList += remoteMetadata
        rootList += recentMetadata
        rootList += favouriteMetadata
        rootList += playlistsMetadata
        mediaIdToChildren[BROWSABLE_ROOT_ID] = rootList

        musicSource.forEach { mediaItem ->
            val albumMediaId = mediaItem.getString(METADATA_KEY_ALBUM)
            val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)
            albumChildren += mediaItem
            Log.d("serviceTAG", "album amount " + mediaIdToChildren[albumMediaId]?.size)
            Log.d(
                "serviceTAG",
                "album " + albumMediaId + " + " + mediaItem.getString(METADATA_KEY_TITLE)
            )
            Log.d(
                "serviceTAG",
                "mediaIdToChildren[" + albumMediaId + "] " + mediaIdToChildren[albumMediaId]
            )

//            if (mediaItem.getString(METADATA_KEY_ALBUM) == recentMediaId) {
//                mediaIdToChildren[RECENT_ROOT_ID] = mutableListOf(mediaItem)
//            }
        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    private fun buildAlbumRoot(mediaItem: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val albumMetadata = Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, mediaItem.getString(METADATA_KEY_ALBUM))
            putString(
                METADATA_KEY_TITLE,
                mediaItem.getString(METADATA_KEY_ALBUM)
            ) // todo get from extras the folder name
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