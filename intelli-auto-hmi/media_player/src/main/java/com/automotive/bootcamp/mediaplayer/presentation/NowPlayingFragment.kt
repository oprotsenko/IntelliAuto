package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.POSITION_BUNDLE
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentNowPlayingBinding
import com.automotive.bootcamp.mediaplayer.enums.RepeatMode
import com.automotive.bootcamp.mediaplayer.viewModels.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.LocalMusicViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowPlayingFragment :
    BaseFragment<FragmentNowPlayingBinding>(FragmentNowPlayingBinding::inflate) {

    private val nowPlayingViewModel: NowPlayingViewModel by viewModel()
    private val localMusicViewModel: LocalMusicViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        nowPlayingViewModel.playSong()
    }

    private fun initView() {
        arguments?.getInt(POSITION_BUNDLE)?.let {
            nowPlayingViewModel.init(
                localMusicViewModel.localMusicData.value,
                it
            )
        }
        
        binding.apply {
            ibNowPlayingPlayPause.setOnClickListener {
                if (nowPlayingViewModel.isPlaying.value == true) {
                    nowPlayingViewModel.pauseSong()
                } else {
                    nowPlayingViewModel.playSong()
                }
            }

            ibNowPlayingNext.setOnClickListener {
                nowPlayingViewModel.nextSong()
            }

            ibNowPlayingPrevious.setOnClickListener {
                nowPlayingViewModel.previousSong()
            }

            ibNowPlayingShuffle.setOnClickListener {
                nowPlayingViewModel.shuffleSongs()
            }

            ibNowPlayingRepeat.setOnClickListener {
                nowPlayingViewModel.nextRepeatMode()
            }
        }
    }

    override fun setListeners() {
        nowPlayingViewModel.currentSong.observe(viewLifecycleOwner) {
            binding.apply {
                ivNowPlayingAlbumArt.setImageBitmap(it.song.cover)
                ivNowPlayingBackground.setImageBitmap(it.song.cover)
//                ivNowPlayingBackground.loadImage(it.cover?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
//                ivNowPlayingAlbumArt.loadImage(it.cover?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
                tvNowPlayingSingerName.text = it.song.artist
                tvNowPlayingSongTitle.text = it.song.title
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
        fun newInstance(position: Int) =
            NowPlayingFragment().apply {
                arguments = Bundle()
                arguments?.putInt(POSITION_BUNDLE, position)
            }
    }
}