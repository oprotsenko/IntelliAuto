package com.automotive.bootcamp.music_browse_service.data

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.automotive.bootcamp.music_browse_service.*
import com.automotive.bootcamp.music_browse_service.data.extensions.*
import com.automotive.bootcamp.music_browse_service.data.remote.AudioAPI
import com.automotive.bootcamp.music_browse_service.data.remote.AudioResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class RetrofitAudioSource(private val audioAPI: AudioAPI) : AbstractAudioSource() {
    companion object {
        const val ORIGINAL_ARTWORK_URI_KEY = "com.automotive.bootcamp.RETROFIT_ARTWORK_URI"
    }

    private var catalog: List<MediaMetadataCompat> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        updateCatalog(Uri.parse(BASE_URL))?.let { updatedCatalog ->
            catalog = updatedCatalog
            state = STATE_INITIALIZED
        } ?: run {
            catalog = emptyList()
            state = STATE_ERROR
        }
    }

    private suspend fun updateCatalog(catalogUri: Uri): List<MediaMetadataCompat>? {
        return withContext(Dispatchers.IO) {
            val response = try {
                audioAPI.getRemoteMusic()
            } catch (ioException: IOException) {
                return@withContext null
            }

            // Get the base URI to fix up relative references later.
            val baseUri = catalogUri.toString().removeSuffix(catalogUri.lastPathSegment ?: "")

            val mediaMetadataCompats = response.body()?.music?.map { audio ->
                // The JSON may have paths that are relative to the source of the JSON
                // itself. We need to fix them up here to turn them into absolute paths.
                catalogUri.scheme?.let { scheme ->
                    if (!audio.source.startsWith(scheme)) {
                        audio.source = baseUri + audio.source
                    }
                    if (!audio.image.startsWith(scheme)) {
                        audio.image = baseUri + audio.image
                    }
                }
                val defaultImageUri = Uri.parse(audio.image)
                val imageUri = AlbumArtContentProvider.mapUri(defaultImageUri)

                MediaMetadataCompat.Builder()
                    .from(audio)
                    .apply {
                        displayIconUri = imageUri.toString() // Used by ExoPlayer and Notification
                        albumArtUri = imageUri.toString()
                        // Keep the original artwork URI for being included in Cast metadata object.
                        putString(ORIGINAL_ARTWORK_URI_KEY, defaultImageUri.toString())
                    }
                    .build()
            }?.toList()
            // Add description keys to be used by the ExoPlayer MediaSession extension when
            // announcing metadata changes.
            mediaMetadataCompats?.forEach { it.description.extras?.putAll(it.bundle) }
            mediaMetadataCompats
        }
    }
}

fun MediaMetadataCompat.Builder.from(audioResponse: AudioResponse): MediaMetadataCompat.Builder {
    val durationMs = TimeUnit.SECONDS.toMillis(audioResponse.duration.toLong())

    audioResponse.let {
        id = it.artist.hashCode().toString()
        title = it.title
        artist = it.artist
        album = it.album
        duration = durationMs
        genre = it.genre
        mediaUri = it.source
        albumArtUri = it.image
        trackNumber = it.trackNumber.toLong()
        trackCount = it.totalTrackCount.toLong()
        flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

        // To make things easier for *displaying* these, set the display properties as well.
        displayTitle = it.title
        displaySubtitle = it.artist
        displayDescription = it.album
        displayIconUri = it.image

        // Add downloadStatus to force the creation of an "extras" bundle in the resulting
        // MediaMetadataCompat object. This is needed to send accurate metadata to the
        // media session during updates.
        downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
    }

    return this
}