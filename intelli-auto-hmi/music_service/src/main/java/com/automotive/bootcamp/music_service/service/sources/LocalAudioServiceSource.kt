package com.automotive.bootcamp.music_service.service.sources

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import com.automotive.bootcamp.music_service.data.LocalMediaRepository
import com.automotive.bootcamp.music_service.service.sources.State.INITIALIZING
import com.automotive.bootcamp.music_service.service.sources.State.INITIALIZED
import com.automotive.bootcamp.music_service.service.utils.LOCAL_ROOT_ID
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class LocalAudioServiceSource(
    private val localRepository: LocalMediaRepository,
) : AbstractMusicSource() {

    var audios = emptyList<MediaMetadataCompat>()

    override fun iterator(): Iterator<MediaMetadataCompat> = audios.iterator()

    suspend fun load() {
        state = INITIALIZING
        audios = localRepository.retrieveLocalAudio().map { audio ->
            Builder()
                .putString(METADATA_KEY_MEDIA_ID, audio.id.toString())
                .putString(METADATA_KEY_ARTIST, audio.artist)
                .putString(METADATA_KEY_TITLE, audio.title)
                .putString(METADATA_KEY_ART_URI, audio.cover)
                .putString(METADATA_KEY_MEDIA_URI, audio.url)
                .putString(METADATA_KEY_DISPLAY_TITLE, audio.title)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
//                .putString(METADATA_KEY_DURATION, audio.duration)
                .putString(METADATA_KEY_ALBUM, LOCAL_ROOT_ID)
                .build()
        }
        state = INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        audios.forEach { audio ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(audio.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = audios.map { audio ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaId(audio.description.mediaId)
            .setTitle(audio.description.title)
            .setSubtitle(audio.description.subtitle)
            .setIconUri(audio.description.iconUri)
            .setMediaUri(audio.description.mediaUri)
            .setExtras(Bundle().apply { putString(METADATA_KEY_DURATION, audio.getString(METADATA_KEY_DURATION)) })
            .build()
        MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)
    }.toMutableList()
}