package com.protsolo.media3_service.sources.models

data class AudioItem(
    val id: Long,
    val cover: String?,
    val title: String?,
    val artist: String?,
    val url: String
)