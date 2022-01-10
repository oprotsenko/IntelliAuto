package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.databinding.MediaPlayerFragmentBinding
import kotlinx.android.synthetic.main.media_player_fragment.*

class MediaPlayerFragment : BaseFragment<MediaPlayerFragmentBinding>(MediaPlayerFragmentBinding::inflate) {
    private lateinit var viewModel: MediaPlayerViewModel
    private lateinit var mediaPlayerAdapter: MediaPlayerRecyclerViewAdapter

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        viewModel = ViewModelProvider(this)[MediaPlayerViewModel::class.java]
        viewModel.albumsList.observe(viewLifecycleOwner) {
            mediaPlayerAdapter.submitList(it)
        }

        initRecyclerView()

        viewModel.getAlbums()
    }

    private fun initRecyclerView(){
        mediaPlayerAdapter = MediaPlayerRecyclerViewAdapter()
        rvAlbums.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = mediaPlayerAdapter
        }
    }
}