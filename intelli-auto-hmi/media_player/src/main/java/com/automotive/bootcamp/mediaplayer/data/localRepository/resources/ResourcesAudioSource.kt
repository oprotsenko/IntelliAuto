package com.automotive.bootcamp.mediaplayer.data.localRepository.resources

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.data.localRepository.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.localRepository.models.AudioItem
import kotlin.system.measureNanoTime

class ResourcesAudioSource(
    private val retriever: MediaMetadataRetriever,
    private val context: Context
) : LocalMedia {
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
                else null
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            list.add(AudioItem(res.toLong(), image, title,artist, duration, audioPath.toString()))
        }
        return list
    }
}