package com.automotive.bootcamp.mediaplayer.presentation

import androidx.fragment.app.Fragment
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {
    private var localAudioFragment = LocalAudioFragment()
    private var onlineAudioFragment = OnlineAudioFragment()
    private var recentAudioFragment = RecentAudioFragment()
    private var favouriteAudioFragment = FavouriteAudioFragment()

    override fun setListeners() {
        binding.apply {
            bLocalMusic.setOnClickListener {
                navigateTo(localAudioFragment)
            }
            bOnlineMusic.setOnClickListener {
                navigateTo(onlineAudioFragment)
            }
            bRecentMusic.setOnClickListener {
                navigateTo(recentAudioFragment)
            }
            bPlaylists.setOnClickListener {
                navigateTo(PlaylistsFragment())
            }
            bFavourite.setOnClickListener {
                navigateTo(favouriteAudioFragment)
            }
            bSearch.setOnClickListener {

            }
            bSettings.setOnClickListener {

            }
        }
    }

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.mediaPlayerServiceContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
