package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContainerView(localAudioFragment)
    }

    override fun setListeners() {
        binding.apply {
            bLocalMusic.setOnClickListener {
                setContainerView(localAudioFragment)
            }
            bOnlineMusic.setOnClickListener {
                setContainerView(onlineAudioFragment)
            }
            bRecentMusic.setOnClickListener {
                setContainerView(recentAudioFragment)
            }
            bPlaylists.setOnClickListener {
                setContainerView(PlaylistsFragment())
            }
            bFavourite.setOnClickListener {
                setContainerView(favouriteAudioFragment)
            }
            bSearch.setOnClickListener {

            }
            bSettings.setOnClickListener {

            }
        }
    }

    private fun setContainerView(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.mediaPlayerServiceContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
