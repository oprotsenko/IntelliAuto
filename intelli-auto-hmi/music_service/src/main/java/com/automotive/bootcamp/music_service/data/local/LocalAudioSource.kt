package com.automotive.bootcamp.music_service.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.automotive.bootcamp.music_service.R
import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.utils.DEFAULT_COVER
import java.io.File
import java.io.FileOutputStream
import java.util.*

class LocalAudioSource(
    private val context: Context
) {

    private val retriever = MediaMetadataRetriever()

    private val media = listOf(
        R.raw.ben_lvcas_love_of_my_life,
        R.raw.bj_wilbanks_change_your_mind,
        R.raw.fresh_body_shop_don_t_need_a_reason,
        R.raw.infraction_sensitive
    )

    fun retrieveLocalAudio(): List<AudioItem> {
        val list = mutableListOf<AudioItem>()
        media.forEach { res ->
            val audioPath = Uri.parse("android.resource://" + context.packageName + "/" + res)
            retriever.setDataSource(context, audioPath)
            val data = retriever.embeddedPicture
            val image =
                if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size)
                else {
                    val inputStream = context.assets.open(DEFAULT_COVER)
                    BitmapFactory.decodeStream(inputStream)
                }
            val imagePath = saveImageToExternalStorage(image)

            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

            list.add(
                AudioItem(
                    res.toLong(),
                    imagePath,
                    title,
                    artist,
                    audioPath.toString()
                )
            )
        }
        return list
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap): String? {
        val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
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