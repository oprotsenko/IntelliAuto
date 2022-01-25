package com.automotive.bootcamp.mediaplayer.data.localRepository.local

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore

import com.automotive.bootcamp.mediaplayer.data.localRepository.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

class LocalAudioSource(
    private val contentResolver: ContentResolver,
    private val retriever: MediaMetadataRetriever
) : LocalMedia {

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
                    if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size)
                    else null
                list.add(AudioItem(id, image, title, artist, duration, url))
            }
        }
        return list
    }
}