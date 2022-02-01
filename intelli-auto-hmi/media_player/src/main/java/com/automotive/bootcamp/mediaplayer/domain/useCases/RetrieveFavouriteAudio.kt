package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudio
import com.automotive.bootcamp.mediaplayer.domain.FavouriteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.extensions.wrapAudio
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RetrieveFavouriteAudio(private val favouriteAudioRepository: FavouriteMediaRepository) {
    fun retrieveFavouriteAudio(): Flow<List<AudioWrapper>?>? {
        return favouriteAudioRepository.getPlaylist()?.map { playlist ->
            playlist?.list?.map {
                it.mapToAudio().wrapAudio()
            }
        }
    }
}