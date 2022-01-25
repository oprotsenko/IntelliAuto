package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioCompletionListener
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.AudioRunningListener
import com.automotive.bootcamp.mediaplayer.utils.AudioPlayer

class AudioPlaybackControl(private val audioPlayer: AudioPlayer) {
    fun setOnAudioCompletionListener(audioCompletionListener: AudioCompletionListener) =
        audioPlayer.setOnAudioCompletionListener(audioCompletionListener)

    fun setOnAudioRunningListener(audioRunningListener: AudioRunningListener) =
        audioPlayer.setOnAudioRunningListener(audioRunningListener)

    fun playAudio(audioUrl: String?) = audioPlayer.playAudio(audioUrl)
    fun pauseAudio() = audioPlayer.pauseAudio()
    fun nextAudio(audioUrl: String?) = audioPlayer.nextAudio(audioUrl)
    fun previousAudio(audioUrl: String?) = audioPlayer.previousAudio(audioUrl)
    fun updateAudioProgress(progress: Int) = audioPlayer.updateAudioProgress(progress)

    fun getShuffledAudioList(
        audioListData: MutableList<AudioWrapper>,
        currentAudio: AudioWrapper
    ): MutableList<AudioWrapper> {
        val audioListMinusCurrentAudio = audioListData.filter {
            it != currentAudio
        }
        val shuffledAudioList = audioListMinusCurrentAudio.shuffled()

        audioListData.clear()
        audioListData.add(currentAudio)
        audioListData.addAll(shuffledAudioList)

        return audioListData
    }
}