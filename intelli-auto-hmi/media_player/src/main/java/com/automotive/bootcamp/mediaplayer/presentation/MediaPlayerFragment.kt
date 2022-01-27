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

                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, LocalMusicFragment())
                    .addToBackStack(null).commit()
            }
            bOnlineMusic.setOnClickListener {
                Log.d("MediaPlayerFragment", "online")

                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, OnlineMusicFragment())
                    .addToBackStack(null).commit()
            }
            bRecentMusic.setOnClickListener {
                Log.d("MediaPlayerFragment", "Recent")

                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, RecentAudioFragment())
                    .addToBackStack(null).commit()
            }
            bPlaylists.setOnClickListener {
                Log.d("MediaPlayerFragment", "Playlists")

                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, PlaylistsFragment())
                    .addToBackStack(null).commit()
            }
            bFavourite.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mediaPlayerServiceContainer, FavouriteMusicFragment())
                    .addToBackStack(null).commit()
            }
            bSearch.setOnClickListener {

            }
            bSettings.setOnClickListener {

            }
        }
    }
}
