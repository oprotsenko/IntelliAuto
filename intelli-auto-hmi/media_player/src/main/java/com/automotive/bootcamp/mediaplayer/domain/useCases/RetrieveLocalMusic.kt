package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class RetrieveLocalMusic(private val repositoryLocal: LocalMediaRepository) {

    suspend fun retrieveLocalMusic() = repositoryLocal.retrieveLocalAudio()

}