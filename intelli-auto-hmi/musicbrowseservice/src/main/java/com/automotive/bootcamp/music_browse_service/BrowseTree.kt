package com.automotive.bootcamp.music_browse_service

import android.content.Context
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*

class BrowseTree(
    val context: Context,
    musicSource: ResourcesAudioSource,
    private val recentMediaId: String? = null
) {
    val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    init {
        val rootList = mediaIdToChildren[BROWSABLE_ROOT_ID] ?: mutableListOf()

        val albumsMetadata = MediaMetadataCompat.Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, ALBUMS_ROOT_ID)
//            putString(METADATA_KEY_TITLE, context.getString(R.string.local_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

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

//        rootList+= albumsMetadata
        rootList += localMetadata
        rootList += remoteMetadata
        rootList += recentMetadata
        rootList += favouriteMetadata
        rootList += playlistsMetadata
        mediaIdToChildren[BROWSABLE_ROOT_ID] = rootList

//        musicSource.forEach { mediaItem ->
//            val albumMediaId = mediaItem.getString(METADATA_KEY_ALBUM)
//            val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)
//            albumChildren += mediaItem
//
//            if (mediaItem.getString(METADATA_KEY_ALBUM) == recentMediaId) {
//                mediaIdToChildren[RECENT_ROOT_ID] = mutableListOf(mediaItem)
//            }
//        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

//    private fun buildAlbumRoot(mediaItem: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
//        val albumMetadata = Builder().apply {
//            putString(METADATA_KEY_MEDIA_ID, RECENT_ROOT_ID) // todo get from extras the folder name
//            putString(
//                METADATA_KEY_TITLE,
//                context.getString(R.string.recent_music)
//            ) // todo get from extras the folder name
//            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
//        }.build()
//
//        val rootList = mediaIdToChildren[ALBUMS_ROOT_ID] ?: mutableListOf()
//        rootList += albumMetadata
//        mediaIdToChildren[ALBUMS_ROOT_ID] = rootList
//
//        return mutableListOf<MediaMetadataCompat>().also {
//            mediaIdToChildren[albumMetadata.getString(METADATA_KEY_MEDIA_ID)] = it
//        }
//    }
}