package com.protsolo.media3_service

import android.util.Log
import androidx.media3.common.MediaMetadata
import com.protsolo.media3_service.sources.ServiceSources

class BrowseTree(
    musicSource: ServiceSources
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadata>>()

    init {
        Log.d("serviceTAG", "source items " + musicSource.sourceList.size)
        musicSource.forEach { mediaItem ->
            val albumMediaId = mediaItem.genre
            val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)
            mediaIdToChildren.forEach {
                Log.d("serviceTAGS", "root -----> " + it.key)
            }
            albumChildren += mediaItem
        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    fun addMetadata(mediaId: String, children: MediaMetadata) {
        val newChildren = mediaIdToChildren[mediaId] ?: mutableListOf()
        newChildren += children
        mediaIdToChildren[mediaId] = newChildren
    }

    private fun buildAlbumRoot(mediaItem: MediaMetadata): MutableList<MediaMetadata> {
        val albumMetadata = MediaMetadata.Builder().apply {
            setGenre(mediaItem.genre)
            setTitle(mediaItem.genre)
            setIsPlayable(false)
        }.build()

        val rootList = mediaIdToChildren[BROWSABLE_ROOT_ID] ?: mutableListOf()
        rootList += albumMetadata
        mediaIdToChildren[BROWSABLE_ROOT_ID] = rootList

        return mutableListOf<MediaMetadata>().also {
            mediaIdToChildren[albumMetadata.genre.toString()] = it
        }
    }
}