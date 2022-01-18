package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class GetLocalMusic(private val repositoryLocal: LocalMediaRepository) {

    suspend fun getLocalSongs() = repositoryLocal.getLocalSongs()

}