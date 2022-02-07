package com.automotive.bootcamp.music_browse_service.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.automotive.bootcamp.music_browse_service.*
import com.automotive.bootcamp.music_browse_service.data.extensions.*
import com.automotive.bootcamp.music_browse_service.data.resources.AudioItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ResourcesAudioSource(
    private val retriever: MediaMetadataRetriever,
    private val context: Context
) : AbstractAudioSource() {
    companion object {
        const val ORIGINAL_ARTWORK_URI_KEY = "com.automotive.bootcamp.RESOURCES_ARTWORK_URI"
    }

    private val media = listOf(
        R.raw.abba_just_a_notion,
        R.raw.alina_libkind_radars,
//        R.raw.blago_white_krasavchik,
//        R.raw.janob_rasul_gejalar,
//        R.raw.stan_sono_inside_your_love,
//        R.raw.summer_feeling,
//        R.raw.two_feet_her_life,
//        R.raw.zivert_cry,
//        R.raw.lana_del_rey_love,
//        R.raw.melisa_ft_tommo_im_alone_2021,
//        R.raw.bob_moses_love_brand_new
    )

    private var catalog: List<MediaMetadataCompat> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        updateCatalog()?.let { updatedCatalog ->
            catalog = updatedCatalog
            state = STATE_INITIALIZED
        } ?: run {
            catalog = emptyList()
            state = STATE_ERROR
        }
    }

    private suspend fun updateCatalog(): List<MediaMetadataCompat>? {
        return withContext(Dispatchers.IO) {
            val audios = mutableListOf<AudioItem>()

            media.forEach { res ->
                val audioPath = Uri.parse("android.resource://" + context.packageName + "/" + res)
                retriever.setDataSource(context, audioPath)
                val data = retriever.embeddedPicture
                val image =
                    if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size)
                    else {
                        val inputStream = context.assets.open("default_cover.jpg")
                        BitmapFactory.decodeStream(inputStream)
                    }
                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

                val imagePath = saveImageToExternalStorage(image)

                audios.add(
                    AudioItem(
                        res.toLong(),
                        imagePath,
                        title,
                        artist,
                        audioPath.toString()
                    )
                )
            }

            val mediaMetadataCompats = audios.map { audio ->
                val defaultImageUri = Uri.parse(audio.cover)
                val imageUri = AlbumArtContentProvider.mapUri(defaultImageUri)

                MediaMetadataCompat.Builder()
                    .from(audio)
                    .apply {
                        displayIconUri = imageUri.toString() // Used by ExoPlayer and Notification
                        albumArtUri = imageUri.toString()
                        // Keep the original artwork URI for being included in Cast metadata object.
                        putString(
                            RetrofitAudioSource.ORIGINAL_ARTWORK_URI_KEY,
                            defaultImageUri.toString()
                        )
                    }
                    .build()
            }.toList()
            // Add description keys to be used by the ExoPlayer MediaSession extension when
            // announcing metadata changes.
            mediaMetadataCompats.forEach { it.description.extras?.putAll(it.bundle) }
            mediaMetadataCompats
        }
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap): String? {
        val directory = context.getDir("Pictures_serv", Context.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }

        val file = File(directory, "${UUID.randomUUID()}.jpg")

        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: Exception) {
            Log.e("ResourcesAudioSource", e.message, e)
        }

        return Uri.parse(file.absolutePath).path
    }
}

fun MediaMetadataCompat.Builder.from(audioItem: AudioItem): MediaMetadataCompat.Builder {
    audioItem.let {
        id = it.id.toString()
        title = it.title
        artist = it.artist
        mediaUri = it.url
        albumArtUri = it.cover
        flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

        // To make things easier for *displaying* these, set the display properties as well.
        displayTitle = it.title
        displaySubtitle = it.artist
        displayIconUri = it.cover

        // Add downloadStatus to force the creation of an "extras" bundle in the resulting
        // MediaMetadataCompat object. This is needed to send accurate metadata to the
        // media session during updates.
        downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
    }

    return this
}