package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_CAPACITY
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect

class AddRecent(
    private val recentAudioRepository: RecentMediaRepository,
    retrieveRecent: RetrieveRecentAudio,
    dispatcher: CoroutineDispatcher
) {
    private val addRecentJob = Job()
    private val addRecentScope = CoroutineScope(dispatcher + addRecentJob)

    private var recentAudiosFlow = retrieveRecent.retrieveRecentAudio()
    private var recentAudios: List<Audio>? = null
    private val channel: Channel<Unit> = Channel(0)

    init {
        addRecentScope.launch {
            recentAudiosFlow.collect {
                recentAudios = it
                channel.trySend(Unit)
            }
        }
    }

    fun execute(aid: Long) {
        addRecentScope.launch {
            if (recentAudioRepository.hasAudio(aid)) {
                recentAudioRepository.removeAudio(aid)
                channel.receive()
            }

            checkPlaylistCapacity()
            recentAudioRepository.addAudio(aid)
        }
    }

    private suspend fun checkPlaylistCapacity() {
        recentAudios?.let { audios ->
            if (audios.size >= RECENT_PLAYLIST_CAPACITY) {
                val aid = audios.first().id
                recentAudioRepository.removeAudio(aid)
            }
        }
    }

    fun onServiceDestroy() {
        addRecentScope.cancel()
    }
}