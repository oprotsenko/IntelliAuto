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
                parentFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.launchMediaPlayer, LocalMusicFragment()).commit()
            }
            bOnlineMusic.setOnClickListener {

            }
            bRecentMusic.setOnClickListener {
                Log.d("MediaPlayerFragment","bRecentMusic")

                parentFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.launchMediaPlayer, RecentAudioFragment()).commit()
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
