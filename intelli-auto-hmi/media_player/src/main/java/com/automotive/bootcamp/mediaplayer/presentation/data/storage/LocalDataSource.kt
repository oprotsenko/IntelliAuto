package com.automotive.bootcamp.mediaplayer.presentation.data.storage

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum

class LocalDataSource:LocalData {

//    val filePath = Environment.getExternalStorageDirectory().path
    val filePath = "/storage/emulated/0/Music/Two Feet – Her Life(seehall.me).mp3"

    override suspend fun getAlbums(): List<MediaAlbum> {
        Log.d("TAG", filePath)
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(filePath)

        val image = metaRetriever.embeddedPicture
        val songTitle = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val list = mutableListOf<MediaAlbum>()
        list.add(MediaAlbum("0", image, songTitle, artist, duration))
        metaRetriever.release()
//        (0..15).forEach { i ->
//            list.add(
//                MediaAlbum(
//                    i.toString(), getRandomImage(),
//                    "$i songTitle", "$i singerName", "$i 5.0",
//                )
//            )
//        }

//        val list = listOf(
//            MediaAlbum("http://","No tears left to cry","Ariana Grande"),
//            MediaAlbum("http://","Enemy","Magdalena"),
//            MediaAlbum("http://","Starboy","The weekend"),
//            MediaAlbum("http://","Pain","Ryan Jones"),
//            MediaAlbum("http://","Plastic Hearts","Miley Cyrus"),
//            MediaAlbum("http://","I am you","Stray Kids"),
//            MediaAlbum("http://","Pain","Ryan Jones"),
//            MediaAlbum("http://","Plastic Hearts","Miley Cyrus"),
//            MediaAlbum("http://","I am you","Stray Kids"),
//            MediaAlbum("http://","Pain","Ryan Jones"),
//            MediaAlbum("http://","Plastic Hearts","Miley Cyrus"),
//            MediaAlbum("http://","I am you","Stray Kids")
//        )

        return list
    }

    private fun getRandomImage(): String {
        var random = (500..5000).random()
        while (Uri.parse("https://cspromogame.ru//storage/upload_images/avatars/$random.jpg") == null)
            random = (500..5000).random()
        return "https://cspromogame.ru//storage/upload_images/avatars/$random.jpg"
    }
}