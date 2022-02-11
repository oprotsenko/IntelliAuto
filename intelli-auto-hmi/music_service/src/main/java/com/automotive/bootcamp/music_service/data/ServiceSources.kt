package com.automotive.bootcamp.music_service.data

import android.content.Context
import android.media.browse.MediaBrowser
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.util.Log
import androidx.core.net.toUri
import com.automotive.bootcamp.music_service.data.remote.RemoteAudioSource
import com.automotive.bootcamp.music_service.utils.AlbumArtContentProvider
import com.automotive.bootcamp.music_service.utils.LOCAL_ROOT_ID
import com.automotive.bootcamp.music_service.utils.METADATA_KEY_FLAGS
import com.automotive.bootcamp.music_service.utils.REMOTE_ROOT_ID
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ServiceSources(
    context: Context
) : AbstractMusicSource() {

    private val localSource = LocalAudioSource(context)
    private val remoteSource = RemoteAudioSource()

    val music = mutableListOf<MediaMetadataCompat>()

    init {
        state = State.INITIALIZING
    }

    override suspend fun load() {
        Log.d("serviceTAG", "loading")
        retrieveLocalAudio()
        retrieveRemoteAudio()
        state = State.INITIALIZED
        Log.d("serviceTAG", music.size.toString())
    }

    private suspend fun retrieveRemoteAudio() {
        val audios = remoteSource.load()?.mapToMediaMetadataCompat(REMOTE_ROOT_ID, true)
        audios?.forEach {
            Log.d("serviceTAG", "cover path " + it.getString(METADATA_KEY_ART_URI))
            music.add(it)
        }
    }

    private fun retrieveLocalAudio() {
        val audios = localSource.retrieveLocalAudio().mapToMediaMetadataCompat(LOCAL_ROOT_ID, false)
        audios.forEach {
            music.add(it)
        }
    }

    private fun List<AudioItem>.mapToMediaMetadataCompat(
        rootId: String,
        isRemote: Boolean
    ): List<MediaMetadataCompat> =
        this.map { audio ->
            val imageUri = AlbumArtContentProvider.mapUri(Uri.parse(audio.cover), isRemote)
            Builder()
                .putString(METADATA_KEY_MEDIA_ID, audio.id.toString())
                .putString(METADATA_KEY_ARTIST, audio.artist)
                .putString(METADATA_KEY_TITLE, audio.title)
                .putString(METADATA_KEY_ART_URI, imageUri.toString())
                .putString(METADATA_KEY_MEDIA_URI, audio.url)
                .putString(METADATA_KEY_DISPLAY_TITLE, audio.title)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, imageUri.toString())
                .putLong(METADATA_KEY_FLAGS, MediaBrowser.MediaItem.FLAG_PLAYABLE.toLong())
                .putString(METADATA_KEY_ALBUM, rootId)
                .build()
        }

//    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
//        val concatenatingMediaSource = ConcatenatingMediaSource()
//        music.forEach { song ->
//            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(song.getString(METADATA_KEY_MEDIA_URI).toUri())
//            concatenatingMediaSource.addMediaSource(mediaSource)
//        }
//        return concatenatingMediaSource
//    }

    override fun iterator(): Iterator<MediaMetadataCompat> = music.iterator()
}