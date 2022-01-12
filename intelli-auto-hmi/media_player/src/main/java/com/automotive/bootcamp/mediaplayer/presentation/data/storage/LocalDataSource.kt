package com.automotive.bootcamp.mediaplayer.presentation.data.storage

import android.net.Uri
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum

class LocalDataSource:LocalData {
    override suspend fun getAlbums(): List<MediaAlbum> {
        val list = mutableListOf<MediaAlbum>()
        (0..15).forEach { i ->
            list.add(
                MediaAlbum(
                    i.toString(), getRandomImage(),
                    "$i songTitle", "$i singerName", "$i 5.0",
                )
            )
        }

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