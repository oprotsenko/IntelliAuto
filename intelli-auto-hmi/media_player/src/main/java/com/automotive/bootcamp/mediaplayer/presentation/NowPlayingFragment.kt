package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentNowPlayingBinding
import com.automotive.bootcamp.mediaplayer.viewModels.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.SongsListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowPlayingFragment :
    BaseFragment<FragmentNowPlayingBinding>(FragmentNowPlayingBinding::inflate) {

    private val nowPlayingViewModel: NowPlayingViewModel by viewModel()
    private val songsListViewModel: SongsListViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        nowPlayingViewModel.playSong()
    }

    private fun initView() {
        nowPlayingViewModel.setAlbumsListData(songsListViewModel.albumsListData.value)
        nowPlayingViewModel.setPosition(songsListViewModel.position)

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
                nowPlayingViewModel.repeatOneSong()
            }
        }
    }

    override fun setListeners() {
        nowPlayingViewModel.currentSong.observe(viewLifecycleOwner) {
            binding.apply {
                ivNowPlayingAlbumArt.setImageBitmap(it.cover)
                ivNowPlayingBackground.setImageBitmap(it.cover)
//                ivNowPlayingBackground.loadImage(it.cover?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
//                ivNowPlayingAlbumArt.loadImage(it.cover?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
                tvNowPlayingSingerName.text = it.artist
                tvNowPlayingSongTitle.text = it.title
            }
        }

        nowPlayingViewModel.isPlaying.observe(viewLifecycleOwner) {
            updatePlayPauseButtonView(it)
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
}