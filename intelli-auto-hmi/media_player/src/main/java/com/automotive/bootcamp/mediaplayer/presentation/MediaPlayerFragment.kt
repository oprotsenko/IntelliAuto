package com.automotive.bootcamp.mediaplayer.presentation

import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {
    override fun setListeners() {
        binding.apply {
            bLocalMusic.setOnClickListener {

            }
            bOnlineMusic.setOnClickListener {

            }
            bRecentMusic.setOnClickListener {

            }
            bPlaylists.setOnClickListener {

            }
            bFavourite.setOnClickListener {

            }
            bSearch.setOnClickListener {

            }
            bSettings.setOnClickListener {

            }
        }
    }
    }
