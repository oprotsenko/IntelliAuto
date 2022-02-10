package com.automotive.bootcamp.mediaplayer.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.extensions.loadImage
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentPlaybackControlsBinding
import com.automotive.bootcamp.mediaplayer.utils.basicService.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.utils.extensions.timeToString

class QuickPlaybackControlsFragment :
    BaseFragment<FragmentPlaybackControlsBinding>(FragmentPlaybackControlsBinding::inflate) {

    var service: AudioPlayerService? = null
    val bound = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                service = (binder as AudioPlayerService.LocalBinder).service
                bound.value = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                bound.value = false
            }
        }

        Intent(requireContext(), AudioPlayerService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
            requireActivity().startService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        service?.clear()
    }

    override fun setObservers() {
        bound.observe(viewLifecycleOwner) {
            service?.currentAudio?.observe(viewLifecycleOwner) {
                binding.apply {
                    it.cover?.let { url ->
                        imagePlaybackControlsArt.loadImage(url)
                    }
                    tvPlaybackControlsAudioArtist.text = it.artist
                    tvPlaybackControlsAudioTitle.text = it.title
                }
            }

            service?.isPlaying?.observe(viewLifecycleOwner) {
                updatePlayPauseButtonView(it)
            }

            service?.currentAudioProgress?.observe(viewLifecycleOwner) {
                binding.apply {
                    tvPlaybackControlsDuration.text = it.timeToString()
                }
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            ibPlaybackControlsPlayPause.setOnClickListener {
                if (service?.isPlaying?.value == true) {
                    service?.pauseAudio()
                } else {
                    service?.playAudio()
                }
            }

            ibPlaybackControlsNext.setOnClickListener {
                service?.nextAudio()
            }

            ibPlaybackControlsPrevious.setOnClickListener {
                service?.previousAudio()
            }
        }
    }

    private fun updatePlayPauseButtonView(isPlaying: Boolean) {
        val bImageResource = if (isPlaying) {
            R.drawable.ic_pause
        } else {
            R.drawable.ic_play
        }

        binding.ibPlaybackControlsPlayPause.setImageResource(bImageResource)
    }
}