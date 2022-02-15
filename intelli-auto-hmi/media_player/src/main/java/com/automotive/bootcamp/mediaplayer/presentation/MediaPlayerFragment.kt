package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding
import com.automotive.bootcamp.mediaplayer.viewModels.MediaPlayerViewModel

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {
    private var localAudioFragment = LocalAudioFragment()
    private var onlineAudioFragment = OnlineAudioFragment()
    private var recentAudioFragment = RecentAudioFragment()
    private var favouriteAudioFragment = FavouriteAudioFragment()

    private val mediaPlayerViewModel: MediaPlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setContainerView(localAudioFragment)

        return view
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
            svField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mediaPlayerViewModel.setQuery(newText)
                    return true
                }
            })

            bSettings.setOnClickListener {

            }
        }
    }

    private fun setContainerView(fragment: Fragment) {
        binding.svField.setQuery("", false)
        binding.svField.isIconified = true

        parentFragmentManager.beginTransaction()
            .replace(R.id.mediaPlayerServiceContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
