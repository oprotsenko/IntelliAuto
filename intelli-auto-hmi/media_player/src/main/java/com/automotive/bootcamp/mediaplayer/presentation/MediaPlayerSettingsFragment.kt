package com.automotive.bootcamp.mediaplayer.presentation

import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerSettingsBinding
import com.automotive.bootcamp.mediaplayer.presentation.adapters.ServicesAdapter
import com.automotive.bootcamp.mediaplayer.utils.serviceFinder.MediaAppDetails

class MediaPlayerSettingsFragment :
    BaseFragment<FragmentMediaPlayerSettingsBinding>(FragmentMediaPlayerSettingsBinding::inflate) {

    override fun initRecyclerView() {
        val adapterServices = ServicesAdapter(
            requireActivity(), R.layout.item_layout_spinner, MediaPlayerFragment.servicesList
        )
        binding.spinnerMediaServices.apply {
            adapter = adapterServices
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position != 0) {
                            selectedServiceToken = MediaPlayerFragment.servicesList[position].sessionToken
                            selectedService = MediaPlayerFragment.servicesList[position]
                        } else {
                            selectedServiceToken = null
                            selectedService = null
                        }
                        Log.d("listTag", position.toString())
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        Toast.makeText(requireContext(), "nothing is selected", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
        }
    }

    companion object {
        var selectedServiceToken: MediaSessionCompat.Token? = null
        var selectedService: MediaAppDetails? = null
    }
}