package com.automotive.bootcamp.mediaplayer.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentNowPlayingBinding
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import com.automotive.bootcamp.mediaplayer.presentation.data.loadImage

class NowPlayingFragment (private val media: MediaAlbum?): BaseFragment<FragmentNowPlayingBinding>(FragmentNowPlayingBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        media?.let {
            binding.apply {
                ivNowPlayingBackground.loadImage(it.artImage?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
                ivNowPlayingAlbumArt.loadImage(it.artImage?: "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png")
                tvNowPlayingSingerName.text = it.singerName
                tvNowPlayingSongTitle.text = it.songTitle
            }
        }
    }
}