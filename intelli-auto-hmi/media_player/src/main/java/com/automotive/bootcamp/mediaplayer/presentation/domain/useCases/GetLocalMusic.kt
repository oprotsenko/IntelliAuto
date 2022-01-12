package com.automotive.bootcamp.mediaplayer.presentation.domain.useCases

import com.automotive.bootcamp.mediaplayer.presentation.domain.AlbumsRepository

class GetLocalMusic(private val repository: AlbumsRepository) {

    suspend fun getAlbums() = repository.getAlbums()

}