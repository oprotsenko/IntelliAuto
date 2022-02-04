package com.automotive.bootcamp.music_service.service.utils.extensions
//
//import android.support.v4.media.MediaBrowserCompat
//import com.automotive.bootcamp.music_service.service.models.Audio
//import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
//
//fun MediaBrowserCompat.MediaItem.mapToAudioWrapper(
//    isFavourite: Boolean = false,
//    isRecent: Boolean = true
//): AudioWrapper =
//    AudioWrapper(
//        audio = Audio(
//            id = description.mediaId?.toLong() ?: 0,
//            cover = description.iconUri.toString(),
//            title = description.title.toString(),
//            artist = description.subtitle.toString(),
//            url = description.mediaUri.toString(),
////            duration = description.extras?.getString(MediaMetadataCompat.METADATA_KEY_DURATION)
//        ),
//        isFavourite = isFavourite,
//        isRecent = isRecent
//    )
