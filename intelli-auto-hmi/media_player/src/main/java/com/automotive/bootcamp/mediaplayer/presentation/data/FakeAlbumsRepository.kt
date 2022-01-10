package com.automotive.bootcamp.mediaplayer.presentation.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeAlbumsRepository:AlbumsRepository {

    val albums = MutableLiveData<List<MediaAlbum>>()

    override suspend fun getAlbums(): LiveData<List<MediaAlbum>> {
        val list = listOf(
            MediaAlbum("http://","No tears left to cry","Ariana Grande"),
            MediaAlbum("http://","Enemy","Magdalena"),
            MediaAlbum("http://","Starboy","The weekend"),
            MediaAlbum("http://","Pain","Ryan Jones"),
            MediaAlbum("http://","Plastic Hearts","Miley Cyrus"),
            MediaAlbum("http://","I am you","Stray Kids"),
            MediaAlbum("http://","Pain","Ryan Jones"),
            MediaAlbum("http://","Plastic Hearts","Miley Cyrus"),
            MediaAlbum("http://","I am you","Stray Kids"),
            MediaAlbum("http://","Pain","Ryan Jones"),
            MediaAlbum("http://","Plastic Hearts","Miley Cyrus"),
            MediaAlbum("http://","I am you","Stray Kids")
        )

        albums.value = list

        return albums
    }

    override suspend fun getAlbum(albumId: String): MediaAlbum {
        TODO("Not yet implemented")
    }
}