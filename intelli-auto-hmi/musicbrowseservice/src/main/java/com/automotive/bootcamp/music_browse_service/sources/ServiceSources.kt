package com.automotive.bootcamp.music_browse_service.sources

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.browse.MediaBrowser
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.automotive.bootcamp.music_browse_service.sources.remote.RemoteAudioSource
import com.automotive.bootcamp.music_browse_service.sources.remote.retrofit.RetrofitAudioSource
import com.automotive.bootcamp.music_browse_service.utils.LOCAL_ROOT_ID
import com.automotive.bootcamp.music_browse_service.utils.METADATA_KEY_FLAGS
import com.automotive.bootcamp.music_browse_service.utils.REMOTE_ROOT_ID

class ServiceSources(
    context: Context
) : AbstractMusicSource() {

    private val localSource = LocalAudioSource(context)
    private val remoteSource = RemoteAudioSource()

    private val music = mutableListOf<MediaMetadataCompat>()

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
        val audios = remoteSource.load()?.map { audio ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
                .putLong(METADATA_KEY_FLAGS, MediaBrowser.MediaItem.FLAG_PLAYABLE.toLong())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, REMOTE_ROOT_ID)
                .build()
        }
        audios?.forEach {
            music.add(it)
        }
    }

    private fun retrieveLocalAudio() {
        val audios = localSource.retrieveLocalAudio().map { audio ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
                .putLong(METADATA_KEY_FLAGS, MediaBrowser.MediaItem.FLAG_PLAYABLE.toLong())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, LOCAL_ROOT_ID)
                .build()
        }
        audios.forEach {
            music.add(it)
        }
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = music.iterator()
}