package com.automotive.bootcamp.music_service.utils

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.MimeTypes

fun MediaMetadataCompat.toMediaItem(): com.google.android.exoplayer2.MediaItem {
    return with(com.google.android.exoplayer2.MediaItem.Builder()) {
        setMediaId(this@toMediaItem.getString(METADATA_KEY_MEDIA_ID))
        setUri(this@toMediaItem.getString(METADATA_KEY_MEDIA_URI))
        setMimeType(MimeTypes.AUDIO_MPEG)
        setMediaMetadata(toMediaItemMetadata())
    }.build()
}

fun MediaMetadataCompat.toMediaItemMetadata(): com.google.android.exoplayer2.MediaMetadata {
    return with(MediaMetadata.Builder()) {
        setTitle(this@toMediaItemMetadata.getString(METADATA_KEY_TITLE))
        setDisplayTitle(this@toMediaItemMetadata.getString(METADATA_KEY_TITLE))
        setAlbumArtist(this@toMediaItemMetadata.getString(METADATA_KEY_ARTIST))
        setAlbumTitle(this@toMediaItemMetadata.getString(METADATA_KEY_ALBUM))
        setComposer(this@toMediaItemMetadata.getString(METADATA_KEY_COMPOSER))
        setTrackNumber(this@toMediaItemMetadata.getLong(METADATA_KEY_TRACK_NUMBER).toInt())
//        setTotalTrackCount(trackCount.toInt())
//        setDiscNumber(discNumber.toInt())
        setWriter(this@toMediaItemMetadata.getString(METADATA_KEY_WRITER))
        setArtworkUri(this@toMediaItemMetadata.getString(METADATA_KEY_ART_URI).toUri())
    }.build()
}
