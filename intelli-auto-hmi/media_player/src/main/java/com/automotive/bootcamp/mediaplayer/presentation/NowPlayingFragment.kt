package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.extensions.loadImage
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentNowPlayingBinding
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.viewModels.MediaPlayerViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.NowPlayingViewModel
import com.automotive.bootcamp.mediaplayer.viewModels.SongsListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowPlayingFragment :
    BaseFragment<FragmentNowPlayingBinding>(FragmentNowPlayingBinding::inflate) {

    private val nowPlayingViewModel: NowPlayingViewModel by viewModel()
    private val mediaPlayerViewModel: SongsListViewModel by sharedViewModel()
    private lateinit var media: Song
    private var position: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        playSong()
    }

    private fun initView() {
        mediaPlayerViewModel.apply {
            selected.value?.let {
                media = it
                albumsListData.value?.let { it ->
                    position = it.indexOf(media)
                }
            }
        }

        media?.let {
            binding.apply {
                ivNowPlayingAlbumArt.setImageBitmap(it.cover)
                ivNowPlayingBackground.setImageBitmap(it.cover)
//                ivNowPlayingBackground.loadImage(it.cover?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
//                ivNowPlayingAlbumArt.loadImage(it.cover?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
                tvNowPlayingSingerName.text = it.artist
                tvNowPlayingSongTitle.text = it.title

                nowPlayingViewModel.isPlaying.observe(viewLifecycleOwner) {
                    updatePlayPauseButtonView(it)
                }

                ibNowPlayingPlayPause.setOnClickListener {
                    if (nowPlayingViewModel.isPlaying.value == true) {
                        onPauseButtonClicked()
                    } else {
                        onPlayButtonClicked()
                    }
                }

                ibNowPlayingNext.setOnClickListener {
                    onNextButtonClicked()
                }

                ibNowPlayingPrevious.setOnClickListener {
                    onPreviousButtonClicked()
                }

                ibNowPlayingShuffle.setOnClickListener {
                    onShuffleButtonClicked();
                }
            }
        }
    }

    private fun playSong() {
        nowPlayingViewModel.playSong(media?.songURL)
    }

    private fun updatePlayPauseButtonView(isPlaying: Boolean) {
        if (isPlaying) {
            binding.ibNowPlayingPlayPause.setImageResource(R.drawable.ic_pause)
        } else {
            binding.ibNowPlayingPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun onPlayButtonClicked() {
        playSong()
    }

    private fun onPauseButtonClicked() {
        nowPlayingViewModel.pauseSong()
    }

    private fun onNextButtonClicked() {
        if (++position == mediaPlayerViewModel.albumsListData.value?.size) {
            position = 0;
        }

        mediaPlayerViewModel.albumsListData.value?.let {
            media = it[position]
            nowPlayingViewModel.nextSong(media?.songURL)
        }
    }

    private fun onPreviousButtonClicked() {
        if (--position < 0) {
            mediaPlayerViewModel.albumsListData.value?.let {
                position = it.size - 1
            }
        }

        mediaPlayerViewModel.albumsListData.value?.let {
            media = it[position]
            nowPlayingViewModel.previousSong(media?.songURL)
        }
    }

    private fun onShuffleButtonClicked() {

    }

    private fun onRepeatOneButtonClicked() {

    }
}