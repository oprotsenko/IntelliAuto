package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.storage.StorageMedia

class RetrieveFavouriteMusic(private val storageAudioRepository: StorageMedia) {

    fun retrieveFavouriteMusic() = storageAudioRepository.re
}