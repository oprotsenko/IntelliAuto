package com.automotive.bootcamp.mediaplayer.data.local.resources

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.automotive.bootcamp.mediaplayer.utils.PICTURES_DIRECTORY_NAME
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.data.local.LocalAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.utils.DEFAULT_COVER
import java.io.*
import java.util.*

class ResourcesAudioSource(
    private val retriever: MediaMetadataRetriever,
    private val context: Context
) : LocalAudioSource {
    private val media = listOf(
        R.raw.abba_just_a_notion,
        R.raw.alina_libkind_radars,
        R.raw.blago_white_krasavchik,
        R.raw.janob_rasul_gejalar,
        R.raw.stan_sono_inside_your_love,
        R.raw.summer_feeling,
        R.raw.two_feet_her_life,
        R.raw.zivert_cry,
        R.raw.lana_del_rey_love,
        R.raw.melisa_ft_tommo_im_alone_2021,
        R.raw.bob_moses_love_brand_new
    )

    override suspend fun retrieveLocalAudio(): List<AudioItem> {
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
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

            val imagePath = saveImageToExternalStorage(image)

            list.add(
                AudioItem(
                    res.toLong(),
                    imagePath,
                    title,
                    artist,
                    duration,
                    audioPath.toString()
                )
            )
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
            Log.e("ResourcesAudioSource", e.message, e)
        }

        return Uri.parse(file.absolutePath).path
    }
}