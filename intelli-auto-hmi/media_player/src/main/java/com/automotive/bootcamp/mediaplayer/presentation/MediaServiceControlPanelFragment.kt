package com.automotive.bootcamp.mediaplayer.presentation

import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaServiceControlPanelBinding

class MediaServiceControlPanelFragment :
    BaseFragment<FragmentMediaServiceControlPanelBinding>(FragmentMediaServiceControlPanelBinding::inflate) {

    override fun setListeners() {

        binding.apply {
            bLocalMusic.setOnClickListener {
                requireActivity().supportFragmentManager.
                beginTransaction().
                replace(R.id.mediaServiceContent, MediaPlayerListingFragment()).
                addToBackStack(null).commit()
            }
        }
    }
}