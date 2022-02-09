package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.FavouriteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RetrieveFavouriteAudio(private val favouriteAudioRepository: FavouriteMediaRepository) {
    fun retrieveFavouriteAudio(): Flow<List<Audio>?>? {
        return favouriteAudioRepository.getPlaylist()?.map { playlist ->
            playlist?.list
        }
    }
}