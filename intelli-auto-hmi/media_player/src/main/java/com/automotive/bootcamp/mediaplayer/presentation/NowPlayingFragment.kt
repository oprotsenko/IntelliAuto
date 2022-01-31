package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.extensions.loadImage
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentNowPlayingBinding
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.PLAYLIST_BUNDLE_KEY
import com.automotive.bootcamp.mediaplayer.utils.POSITION_BUNDLE_KEY
import com.automotive.bootcamp.mediaplayer.utils.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.utils.extensions.timeToString
import com.automotive.bootcamp.mediaplayer.viewModels.nowPlaying.NowPlayingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowPlayingFragment :
    BaseFragment<FragmentNowPlayingBinding>(FragmentNowPlayingBinding::inflate) {

    private val nowPlayingViewModel: NowPlayingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist: PlaylistWrapper? = arguments?.getParcelable(PLAYLIST_BUNDLE_KEY)
        val position = arguments?.getInt(POSITION_BUNDLE_KEY)
        if (playlist != null && position != null) {
            nowPlayingViewModel.init(
                playlist, position
            )
            nowPlayingViewModel.playAudio()
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
                if (nowPlayingViewModel.isPlaying.value == true) {
                    nowPlayingViewModel.pauseAudio()
                } else {
                    nowPlayingViewModel.playAudio()
                }
            }

            ibNowPlayingNext.setOnClickListener {
                nowPlayingViewModel.nextAudio()
            }

            ibNowPlayingPrevious.setOnClickListener {
                nowPlayingViewModel.previousAudio()
            }

            ibNowPlayingShuffle.setOnClickListener {
                nowPlayingViewModel.shuffleAudio()
            }

            ibNowPlayingRepeat.setOnClickListener {
                nowPlayingViewModel.nextRepeatMode()
            }

            sbNowPlayingProgress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        nowPlayingViewModel.updateAudioProgress(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }

    override fun setObservers() {
        nowPlayingViewModel.currentAudio.observe(viewLifecycleOwner) {
            binding.apply {
                it.audio.cover?.let{ url ->
                    ivNowPlayingAudioArt.loadImage(url)
                    ivNowPlayingBackground.loadImage(url)
                }

                tvNowPlayingSingerName.text = it.audio.artist
                tvNowPlayingAudioTitle.text = it.audio.title
            }
        }

        nowPlayingViewModel.isPlaying.observe(viewLifecycleOwner) {
            updatePlayPauseButtonView(it)
        }

        nowPlayingViewModel.isShuffled.observe(viewLifecycleOwner) {
            updateShuffleButtonView(it)
        }

        nowPlayingViewModel.repeatMode.observe(viewLifecycleOwner) {
            updateRepeatButtonView(it)
        }

        nowPlayingViewModel.currentAudioDuration.observe(viewLifecycleOwner) {
            binding.apply {
                tvNowPlayingAudioDuration.text = it.timeToString()
                sbNowPlayingProgress.max = it
            }
        }

        nowPlayingViewModel.currentAudioProgress.observe(viewLifecycleOwner) {
            binding.apply {
                sbNowPlayingProgress.progress = it
                tvNowPlayingProgress.text = it.timeToString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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