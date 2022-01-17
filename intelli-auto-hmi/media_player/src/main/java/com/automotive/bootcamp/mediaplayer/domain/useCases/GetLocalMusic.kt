package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.MediaRepository

class GetLocalMusic(private val repository: MediaRepository) {

    suspend fun getAlbums() = repository.getAlbums()

}