package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.databinding.FragmentNowPlayingBinding
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import com.automotive.bootcamp.mediaplayer.presentation.data.loadCircleImage

class NowPlayingFragment (private val media: MediaAlbum?): BaseFragment<FragmentNowPlayingBinding>(FragmentNowPlayingBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        media?.let {
            binding.apply {
                ivNowPlayingBackground.loadCircleImage(it.artImage)
                ivNowPlayingAlbumArt.loadCircleImage(it.artImage)
                tvNowPlayingSingerName.text = it.singerName
                tvNowPlayingSongTitle.text = it.songTitle
            }
        }
    }
}