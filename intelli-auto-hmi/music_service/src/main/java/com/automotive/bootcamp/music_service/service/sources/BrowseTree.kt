package com.automotive.bootcamp.music_service.service.sources

import android.content.Context
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import com.automotive.bootcamp.music_service.R
import com.automotive.bootcamp.music_service.service.utils.*

class BrowseTree(
    val context: Context,
    musicSource: MusicSource,
    private val recentMediaId: String? = null
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    init {
        val rootList = mediaIdToChildren[BROWSABLE_ROOT_ID] ?: mutableListOf()

        val albumsMetadata = Builder().apply {
            putString(METADATA_KEY_ALBUM, ALBUMS_ROOT_ID)
//            putString(METADATA_KEY_TITLE, context.getString(R.string.local_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val localMetadata = Builder().apply {
            putString(METADATA_KEY_ALBUM, LOCAL_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.local_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val remoteMetadata = Builder().apply {
            putString(METADATA_KEY_ALBUM, REMOTE_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.online_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val recentMetadata = Builder().apply {
            putString(METADATA_KEY_ALBUM, RECENT_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.recent_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val favouriteMetadata = Builder().apply {
            putString(METADATA_KEY_ALBUM, FAVOURITE_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.favourite_music))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val playlistsMetadata = Builder().apply {
            putString(METADATA_KEY_ALBUM, PLAYLISTS_ROOT_ID)
            putString(METADATA_KEY_TITLE, context.getString(R.string.playlists))
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        rootList+= albumsMetadata
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

            if (mediaItem.getString(METADATA_KEY_ALBUM) == recentMediaId) {
                mediaIdToChildren[RECENT_ROOT_ID] = mutableListOf(mediaItem)
            }
        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    private fun buildAlbumRoot(mediaItem: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val albumMetadata = Builder().apply {
            putString(METADATA_KEY_MEDIA_ID, RECENT_ROOT_ID) // todo get from extras the folder name
            putString(
                METADATA_KEY_TITLE,
                context.getString(R.string.recent_music)
            ) // todo get from extras the folder name
            putLong(METADATA_KEY_FLAGS, FLAG_BROWSABLE.toLong())
        }.build()

        val rootList = mediaIdToChildren[ALBUMS_ROOT_ID] ?: mutableListOf()
        rootList += albumMetadata
        mediaIdToChildren[ALBUMS_ROOT_ID] = rootList

        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[albumMetadata.getString(METADATA_KEY_MEDIA_ID)] = it
        }
    }
}