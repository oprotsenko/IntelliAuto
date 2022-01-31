package com.automotive.bootcamp.mediaplayer.presentation

import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {

    override fun setListeners() {
        binding.apply {
            bLocalMusic.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, LocalAudioFragment())
                    .addToBackStack(null).commit()
            }
            bOnlineMusic.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, OnlineAudioFragment())
                    .addToBackStack(null).commit()
            }
            bRecentMusic.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, RecentAudioFragment())
                    .addToBackStack(null).commit()
            }
            bPlaylists.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, PlaylistsFragment())
                    .addToBackStack(null).commit()
            }
            bFavourite.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, FavouriteAudioFragment())
                    .addToBackStack(null).commit()
            }
            bSearch.setOnClickListener {

            }
            bSettings.setOnClickListener {

            }
        }
    }
}
