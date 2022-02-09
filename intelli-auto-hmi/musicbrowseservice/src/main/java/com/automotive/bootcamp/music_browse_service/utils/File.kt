package com.automotive.bootcamp.music_browse_service.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.File

fun File.asAlbumArtContentUri(): Uri {
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .authority(AUTHORITY)
        .appendPath(this.path)
        .build()
}

private const val AUTHORITY = "com.automotive.bootcamp"