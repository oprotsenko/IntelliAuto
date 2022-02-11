package com.automotive.bootcamp.music_service.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.automotive.bootcamp.music_service.R
import java.io.File
import java.io.FileOutputStream
import java.util.*

class LocalAudioSource(
    private val context: Context
) {

    private val retriever = MediaMetadataRetriever()

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

    fun retrieveLocalAudio(): List<AudioItem> {
        val list = mutableListOf<AudioItem>()
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
            val imagePath = saveImageToExternalStorage(image)
            Log.d("serviceTAG", "cover url " + imagePath)

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