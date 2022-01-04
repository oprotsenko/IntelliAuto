package com.automotive.bootcamp.mediaplayer.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.automotive.bootcamp.mediaplayer.R

class MediaPlayerFragment : Fragment() {


    private lateinit var viewModel: MediaPlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_player_fragment, container, false)
    }


}