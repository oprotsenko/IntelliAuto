package com.automotive.bootcamp.mediaplayer.data.remote.retrofit.responses

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.utils.BASE_URL

data class RemoteMusicResponse(
    val music: List<AudioResponse>
)

data class AudioResponse(
    val title: String,
    val album: String,
    val artist: String,
    val genre: String,
    val source: String,
    val image: String,
    val trackNumber: Int,
    val totalTrackCount: Int,
    val duration: Int,
    val site: String
)

fun AudioResponse.mapToAudioItem() =
    AudioItem(
        id = this.artist.hashCode().toLong(),
        cover = BASE_URL + this.image,
        title = this.title,
        artist = this.artist,
        duration = this.duration.toString(),
        url = BASE_URL + this.source
    )
