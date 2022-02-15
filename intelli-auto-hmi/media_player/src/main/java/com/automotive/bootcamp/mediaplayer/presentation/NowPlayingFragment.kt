package com.automotive.bootcamp.mediaplayer.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.MutableLiveData
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.extensions.loadImage
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentNowPlayingBinding
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.PLAYLIST_BUNDLE_KEY
import com.automotive.bootcamp.mediaplayer.utils.POSITION_BUNDLE_KEY
import com.automotive.bootcamp.mediaplayer.utils.basicService.AudioPlayerService
import com.automotive.bootcamp.mediaplayer.utils.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.utils.extensions.timeToString
import com.automotive.bootcamp.mediaplayer.utils.serviceFinder.MediaAppDetails
import java.util.ArrayList

class NowPlayingFragment :
    BaseFragment<FragmentNowPlayingBinding>(FragmentNowPlayingBinding::inflate) {


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

    override fun setListeners() {
        binding.apply {
            ibNowPlayingBack.setOnClickListener {
                if (parentFragmentManager.backStackEntryCount > 0) {
                    parentFragmentManager.popBackStack()
                }
            }

            ibNowPlayingPlayPause.setOnClickListener {
                service?.apply {
                    if (isPlaying.value == true) {
                        pauseAudio()
                    } else {
                        playAudio()
                    }
                }
            }

            ibNowPlayingNext.setOnClickListener {
                service?.nextAudio()
            }

            ibNowPlayingPrevious.setOnClickListener {
                service?.previousAudio()
            }

            ibNowPlayingShuffle.setOnClickListener {
                service?.shuffleAudio()
            }

            ibNowPlayingRepeat.setOnClickListener {
                service?.nextRepeatMode()
            }

            sbNowPlayingProgress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        service?.updateAudioProgress(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }

    override fun setObservers() {
        bound.observe(viewLifecycleOwner) {
            val playlist: PlaylistWrapper? = arguments?.getParcelable(PLAYLIST_BUNDLE_KEY)
            val position = arguments?.getInt(POSITION_BUNDLE_KEY)

            if (playlist != null && position != null && bound.value == true) {
                service?.apply {
                    init(playlist, position)
                    playAudio()
                }
            }

            service?.currentAudio?.observe(viewLifecycleOwner) {
                binding.apply {
                    it.cover?.let { url ->
                        ivNowPlayingAudioArt.loadImage(url)
                        ivNowPlayingBackground.loadImage(url)
                    }

                    tvNowPlayingSingerName.text = it.artist
                    tvNowPlayingAudioTitle.text = it.title
                }
            }
            service?.isPlaying?.observe(viewLifecycleOwner) {
                updatePlayPauseButtonView(it)
            }

            service?.isShuffled?.observe(viewLifecycleOwner) {
                updateShuffleButtonView(it)
            }

            service?.repeatMode?.observe(viewLifecycleOwner) {
                updateRepeatButtonView(it)
            }

            service?.currentAudioDuration?.observe(viewLifecycleOwner) {
                binding.apply {
                    tvNowPlayingAudioDuration.text = it.timeToString()
                    sbNowPlayingProgress.max = it
                }
            }

            service?.currentAudioProgress?.observe(viewLifecycleOwner) {
                binding.apply {
                    sbNowPlayingProgress.progress = it
                    tvNowPlayingProgress.text = it.timeToString()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        service?.clear()
        arguments = null
    }

    private fun updatePlayPauseButtonView(isPlaying: Boolean) {
        val bImageResource = if (isPlaying) {
            R.drawable.ic_pause
        } else {
            R.drawable.ic_play
        }

        binding.ibNowPlayingPlayPause.setImageResource(bImageResource)
    }

    private fun updateShuffleButtonView(isShuffled: Boolean) {
        binding.ibNowPlayingShuffle.alpha =
            when (isShuffled) {
                true -> 1f
                false -> 0.5f
            }
    }

    private fun updateRepeatButtonView(repeatMode: RepeatMode) {
        binding.ibNowPlayingRepeat.alpha = 1f
        binding.ibNowPlayingRepeat.setImageResource(R.drawable.ic_repeat)

        when (repeatMode) {
            RepeatMode.DEFAULT -> {
                binding.ibNowPlayingRepeat.alpha = 0.5f
            }
            RepeatMode.REPEAT_ONE -> {
                binding.ibNowPlayingRepeat.setImageResource(R.drawable.ic_repeat_one)
            }
            else -> Unit
        }
    }

    companion object {
        fun newInstance(media: PlaylistWrapper, position: Int) =
            NowPlayingFragment().apply {
                arguments = Bundle()
                arguments?.putParcelable(PLAYLIST_BUNDLE_KEY, media)
                arguments?.putInt(POSITION_BUNDLE_KEY, position)
            }
    }
}