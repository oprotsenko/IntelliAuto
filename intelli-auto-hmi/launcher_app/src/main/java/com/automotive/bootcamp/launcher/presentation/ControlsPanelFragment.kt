package com.automotive.bootcamp.launcher.presentation

import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.launcher.R
import com.automotive.bootcamp.launcher.databinding.FragmentControlsPanelBinding
import com.automotive.bootcamp.mediaplayer.presentation.LaunchMediaPlayerFragment

class ControlsPanelFragment :
    BaseFragment<FragmentControlsPanelBinding>(FragmentControlsPanelBinding::inflate) {

    override fun setListeners() {
        binding.apply {
            ibMusic.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.containerControls, LaunchMediaPlayerFragment()).commit()
            }

            ibClimate.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.containerControls, WelcomeFragment()).commit()
            }
        }
    }
}