package com.automotive.bootcamp.mediaplayer.data.local.device

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.automotive.bootcamp.mediaplayer.data.local.LocalAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.utils.DEFAULT_COVER
import com.automotive.bootcamp.mediaplayer.utils.PICTURES_DIRECTORY_NAME
import java.io.File
import java.io.FileOutputStream
import java.util.*

class LocalAudioSource(
    private val contentResolver: ContentResolver,
    private val retriever: MediaMetadataRetriever,
    private val context: Context
) : LocalAudioSource {

    override suspend fun retrieveLocalAudio(): List<AudioItem> {
        val list = mutableListOf<AudioItem>()
        val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )
        contentResolver.query(
            collection,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol)
                val artist = cursor.getString(artistCol)
                val duration = cursor.getString(durationCol)
                val url = cursor.getString(dataCol)
                retriever.setDataSource(url)
                val data = retriever.embeddedPicture
                val image =
                    if (data != null) {
                        BitmapFactory.decodeByteArray(data, 0, data.size)
                    } else {
                        val inputStream = context.assets.open(DEFAULT_COVER)
                        BitmapFactory.decodeStream(inputStream)
                    }

                val imagePath = saveImageToExternalStorage(image)

                list.add(AudioItem(id, imagePath, title, artist, duration, url))
            }
        }
        return list
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap): String? {
        val directory = context.getDir(PICTURES_DIRECTORY_NAME, Context.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }

        val file = File(directory, "${UUID.randomUUID()}.jpg")

        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: Exception) {
            Log.e("LocalAudioSource", e.message, e)
        }

        return Uri.parse(file.absolutePath).path
    }
}