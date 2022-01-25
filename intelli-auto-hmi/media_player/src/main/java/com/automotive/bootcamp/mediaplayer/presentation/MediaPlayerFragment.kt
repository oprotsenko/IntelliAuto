package com.automotive.bootcamp.mediaplayer.presentation

import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding
import com.automotive.bootcamp.mediaplayer.presentation.models.FavouriteMusicFragment

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {

    override fun setListeners() {
        binding.apply {
            bLocalMusic.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, LocalMusicFragment()).commit()
            }
            bOnlineMusic.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, OnlineMusicFragment()).commit()
            }
            bRecentMusic.setOnClickListener {

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, RecentAudioFragment()).commit()
            }
            bPlaylists.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, PlaylistsFragment()).commit()
            }
            bFavourite.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, FavouriteMusicFragment()).commit()
            }
            bSearch.setOnClickListener {

            }
            bSettings.setOnClickListener {

            }
        }
    }
    }
