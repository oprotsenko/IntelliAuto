package com.automotive.bootcamp.music_browse_service.sources.remote.retrofit.responses

import com.automotive.bootcamp.music_browse_service.AudioItem

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
        cover = "https://storage.googleapis.com/automotive-media/" + this.image,
        title = this.title,
        artist = this.artist,
        url = "https://storage.googleapis.com/automotive-media/" + this.source
    )
