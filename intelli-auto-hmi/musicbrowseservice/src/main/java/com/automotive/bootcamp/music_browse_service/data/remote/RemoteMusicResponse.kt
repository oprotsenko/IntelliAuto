package com.automotive.bootcamp.music_browse_service.data.remote

data class RemoteMusicResponse(
    val music: List<AudioResponse>
)

data class AudioResponse(
    val title: String,
    val album: String,
    val artist: String,
    val genre: String,
    var source: String,
    var image: String,
    val trackNumber: Int,
    val totalTrackCount: Int,
    val duration: Int,
    val site: String
)