package com.automotive.bootcamp.mediaplayer.presentation

import android.util.Log
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {

    override fun setListeners() {
        binding.apply {
            bLocalMusic.setOnClickListener {
                Log.d("MediaPlayerFragment", "local")

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, LocalMusicFragment()).commit()
            }
            bOnlineMusic.setOnClickListener {
                Log.d("MediaPlayerFragment", "online")

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, OnlineMusicFragment()).commit()
            }
            bRecentMusic.setOnClickListener {
                Log.d("MediaPlayerFragment", "Recent")

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, RecentAudioFragment()).commit()
            }
            bPlaylists.setOnClickListener {
                Log.d("MediaPlayerFragment", "Playlists")

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
